package com.mgaye.banking_backend.service;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.exception.AccountNotFoundException;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;

import lombok.*;
import jakarta.transaction.Transactional;

// TransactionService.java
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepo;
    private final BankAccountRepository accountRepo;
    private final AuditService auditService;

    public Transaction processTransaction(TransactionRequest request) {
        BankAccount account = accountRepo.findById(request.accountId())
                .orElseThrow(() -> new AccountNotFoundException(request.accountId()));

        validateTransaction(account, request);

        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(request.amount())
                .currency(request.currency())
                .type(request.type())
                .status(TransactionStatus.PENDING)
                .build();

        // Double-entry bookkeeping
        processDoubleEntry(transaction, account);

        auditService.logTransaction(transaction);
        return transactionRepo.save(transaction);
    }

    private void processDoubleEntry(Transaction tx, BankAccount account) {
        switch (tx.getType()) {
            case DEPOSIT -> account.setBalance(account.getBalance() + tx.getAmount());
            case WITHDRAWAL -> {
                validateSufficientBalance(account, tx.getAmount());
                account.setBalance(account.getBalance() - tx.getAmount());
            }
            case TRANSFER -> processTransfer(tx);
        }
    }

    // Additional methods...
}