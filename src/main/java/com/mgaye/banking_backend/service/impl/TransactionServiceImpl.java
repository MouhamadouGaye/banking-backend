// TransactionServiceImpl.java
package com.mgaye.banking_backend.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.controller.TransactionNotificationController;
import com.mgaye.banking_backend.dto.request.TransactionNotificationRequest;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.exception.TransactionNotFoundException;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.AccountRepository;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;
import com.mgaye.banking_backend.service.AuditService;
import com.mgaye.banking_backend.service.TransactionService;
import com.mgaye.banking_backend.validation.TransactionValidator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepo;
    private final BankAccountRepository accountRepo;
    private final MerchantRepository merchantRepo;
    private final AuditService auditService;
    private final TransactionValidator transactionValidator;

    public Transaction create(TransactionRequest request, User user) {
        BankAccount account = accountRepo.findByIdAndUser(request.accountId(), user)
                .orElseThrow(() -> new AccountNotFoundException(request.accountId()));

        transactionValidator.validate(request, account);

        Transaction transaction = buildTransaction(request, account);
        processDoubleEntry(transaction, account);

        Transaction savedTransaction = transactionRepo.save(transaction);
        accountRepo.save(account);

        auditService.logTransaction(savedTransaction, user);
        return savedTransaction;
    }

    public Transaction getTransaction(String id) {
        return transactionRepo.findById(id)
                .orElseThrow(() -> new TransactionNotFoundExcepti(id));
    }

    private Transaction buildTransaction(TransactionRequest request, BankAccount account) {
        return Transaction.builder()
                .account(account)
                .amount(request.amount())
                .currency(request.currency())
                .type(request.type())
                .direction(request.direction())
                .status(TransactionStatus.PENDING)
                .description(request.description())
                .referenceId(request.referenceId())
                .merchant(
                        request.merchantId() != null ? merchantRepo.findById(request.merchantId()).orElse(null) : null)
                .timestamp(Instant.now())
                .build();
    }

    private void processDoubleEntry(Transaction tx, BankAccount account) {
        switch (tx.getType()) {
            case DEPOSIT -> account.credit(tx.getAmount());
            case WITHDRAWAL -> account.debit(tx.getAmount());
            case TRANSFER -> processTransfer(tx);
        }
    }

    private void processTransfer(Transaction tx) {
        // Implementation for transfer transactions
        // Would involve finding the target account and processing both sides
    }

    private final TransactionNotificationController notificationController;

    @Transactional
    public Transaction processTransaction(TransactionRequest request, User user) {
        // ... existing transaction processing ...

        Transaction savedTransaction = transactionRepo.save(transaction);
        notificationController.notifyTransaction(
                new TransactionNotificationRequest(savedTransaction.getId()),
                user);

        return savedTransaction;
    }

    public Transaction getTransactionForUser(String transactionId, String userId) {
        return transactionRepo.findByIdAndUserId(transactionId, userId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }
}