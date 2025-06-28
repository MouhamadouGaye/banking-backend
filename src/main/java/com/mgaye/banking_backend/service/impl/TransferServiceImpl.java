package com.mgaye.banking_backend.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.TransferResult;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.exception.AccountNotFoundException;
import com.mgaye.banking_backend.exception.InsufficientFundsException;
import com.mgaye.banking_backend.exception.TransferValidationException;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.Transaction.TransactionType;
import com.mgaye.banking_backend.repository.AccountRepository;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.service.TransactionService;
import com.mgaye.banking_backend.service.TransferService;
import com.mgaye.banking_backend.service.TransferValidationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {

    private final BankAccountRepository accountRepository;
    private final TransactionService transactionService;
    private final TransferValidationService validationService;

    @Override
    @Transactional
    public Transaction executeTransfer(TransactionRequest request) {

        // Validate this is actually a transfer request
        if (!request.isTransfer()) {
            throw new TransferValidationException("Transaction type must be TRANSFER");
        }

        // Lookup accounts
        BankAccount sourceAccount = accountRepository.findById(UUID.fromString(request.accountId()))
                .orElseThrow(() -> new AccountNotFoundException(request.accountId()));

        BankAccount destinationAccount = accountRepository.findById(UUID.fromString(request.destinationAccountId()))
                .orElseThrow(() -> new AccountNotFoundException(request.destinationAccountId()));

        // Currency validation
        if (!sourceAccount.getCurrency().equals(request.currency()) ||
                !destinationAccount.getCurrency().equals(request.currency())) {
            throw new TransferValidationException("Currency mismatch in transfer request");
        }

        // Check daily limit
        BigDecimal newDailyTotal = sourceAccount.getDailyTransferTotal().add(request.amount());
        if (sourceAccount.getDailyTransferLimit() != null &&
                newDailyTotal.compareTo(sourceAccount.getDailyTransferLimit()) > 0) {
            throw new TransferValidationException(
                    "Daily transfer limit exceeded. Limit: " +
                            sourceAccount.getDailyTransferLimit());
        }

        // Business validation
        validationService.validateTransfer(
                sourceAccount,
                destinationAccount,
                request.amount());

        // Execute balance changes
        if (request.isDebit()) {
            // Handle INBOUND transfer (money coming to source account)
            destinationAccount.debit(request.amount());
            sourceAccount.credit(request.amount());
        } else {
            // Handle OUTBOUND transfer (default)
            sourceAccount.debit(request.amount());
            destinationAccount.credit(request.amount());
        }

        // Record transaction
        return transactionService.create(
                request.withDescription(
                        request.description() != null ? request.description()
                                : String.format("Transfer %s %s to %s",
                                        request.amount(),
                                        request.currency(),
                                        destinationAccount.getAccountNumber())),
                null // User context can be added here if needed
        );
    }

    @Override
    @Transactional
    public List<Transaction> executeBatchTransfers(List<TransactionRequest> requests) {
        return requests.stream()
                .map(req -> {
                    try {
                        return executeTransfer(req);
                    } catch (Exception e) {
                        log.error("Failed to process transfer {}: {}", req.referenceId(), e.getMessage());
                        return null; // or create a failed transaction record
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}