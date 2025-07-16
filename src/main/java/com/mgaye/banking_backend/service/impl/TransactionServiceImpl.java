// TransactionServiceImpl.java
package com.mgaye.banking_backend.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.controller.TransactionNotificationController;
import com.mgaye.banking_backend.dto.request.TransactionNotificationRequest;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.exception.AccountNotFoundException;
import com.mgaye.banking_backend.exception.BankingException;
import com.mgaye.banking_backend.exception.CurrencyMismatchException;
import com.mgaye.banking_backend.exception.InsufficientFundsException;
import com.mgaye.banking_backend.exception.TransactionNotFoundException;
import com.mgaye.banking_backend.model.AuditLogEntry;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.Transaction.TransactionDirection;
import com.mgaye.banking_backend.model.Transaction.TransactionStatus;
import com.mgaye.banking_backend.model.Transaction.TransactionType;
import com.mgaye.banking_backend.repository.AccountRepository;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.MerchantRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;
import com.mgaye.banking_backend.service.AuditService;
import com.mgaye.banking_backend.service.TransactionService;
import com.mgaye.banking_backend.validation.TransactionValidator;

import jakarta.transaction.InvalidTransactionException;
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
        private final TransactionNotificationController notificationController;

        @Override
        public Transaction create(TransactionRequest request, User user) {
                // Step 1: Fetch the user's account
                BankAccount account = accountRepo.findByIdAndUser(
                                UUID.fromString(request.accountId()), user)
                                .orElseThrow(() -> new AccountNotFoundException(request.accountId()));

                // Step 2: Validate the transaction
                try {
                        transactionValidator.validate(request, account);
                } catch (InvalidTransactionException e) {
                        throw new BankingException(
                                        "INVALID_TRANSACTION",
                                        e.getMessage(),
                                        HttpStatus.BAD_REQUEST,
                                        e);

                }

                // Step 3: Build and process the transaction
                Transaction transaction = buildTransaction(request, account);
                processDoubleEntry(transaction, account);

                // Step 4: Persist the transaction and update account
                Transaction savedTransaction = transactionRepo.save(transaction);
                accountRepo.save(account);

                // Step 5: Audit logging
                auditService.logTransaction(savedTransaction, user);

                // Step 6: Return the created transaction
                return savedTransaction;
        }

        @Override
        public List<Transaction> getAccountTransactions(UUID accountId, LocalDate startDate, LocalDate endDate) {
                BankAccount account = accountRepo.findById(accountId)
                                .orElseThrow(() -> new AccountNotFoundException(accountId.toString()));

                Instant startInstant = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
                Instant endInstant = endDate.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC); // inclusive end date

                return transactionRepo.findByAccountAndTimestampBetween(account, startInstant, endInstant);
        }

        @Override
        public void recordAccountStatusChange(UUID accountId, String newStatus, String reason) {
                BankAccount account = accountRepo.findById(accountId)
                                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

                // Use auditService instead of directly using repository
                auditService.recordAccountStatusChange(
                                accountId,
                                newStatus,
                                "Changed from: " + account.getStatus() + ". Reason: " + reason,
                                account.getUser());
        }

        @Override // Add @Override annotation
        public Transaction getTransaction(String id) {
                return transactionRepo.findById(id)
                                .orElseThrow(() -> new TransactionNotFoundException(id));
        }

        @Override // Add @Override annotation
        public Transaction processTransaction(TransactionRequest request, User user) {
                Transaction transaction = create(request, user);
                notificationController.notifyTransaction(
                                new TransactionNotificationRequest(transaction.getId()),
                                user);
                return transaction;
        }

        @Override // Add @Override annotation
        public Transaction getTransactionForUser(String transactionId, UUID userId) {
                return transactionRepo.findByIdAndUserId(transactionId, userId)
                                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
        }

        // ... rest of the methods remain the same ...

        public Transaction buildTransaction(TransactionRequest request, BankAccount account) {
                return Transaction.builder()
                                .account(account)
                                .amount(request.amount())
                                .currency(request.currency())
                                .type(request.type())
                                .direction(Transaction.TransactionDirection.valueOf(request.direction().name()))
                                .status(Transaction.TransactionStatus.PENDING)
                                .description(request.description())
                                .referenceNumber(request.referenceId())
                                .merchant(
                                                request.merchantId() != null ? merchantRepo
                                                                .findById(request.merchantId()).orElse(null) : null)
                                .destinationAccount(request.destinationAccountId() != null
                                                ? accountRepo.getReferenceById(
                                                                UUID.fromString(request.destinationAccountId()))
                                                : null)
                                .timestamp(Instant.now())
                                .build();
        }

        public void processDoubleEntry(Transaction tx, BankAccount account) {
                switch (tx.getType()) {
                        case DEPOSIT -> account.credit(tx.getAmount());
                        case WITHDRAWAL -> account.debit(tx.getAmount());
                        case TRANSFER -> processTransfer(tx);
                }
        }

        public void processTransfer(Transaction tx) {
                if (tx.getDestinationAccount() == null) {

                        throw new BankingException(
                                        "MISSING_DESTINATION_ACCOUNT",
                                        "Transfer transaction requires destination account",
                                        HttpStatus.BAD_REQUEST);
                }

                BankAccount sourceAccount = tx.getAccount();
                BankAccount destinationAccount = accountRepo.findById(tx.getDestinationAccount().getId())
                                .orElseThrow(() -> new AccountNotFoundException(
                                                tx.getDestinationAccount().getId().toString()));

                // Validate currencies match
                if (!sourceAccount.getCurrency().equals(destinationAccount.getCurrency())) {
                        throw new BankingException(
                                        "CURRENCY_MISMATCH",
                                        "Source and destination accounts must have same currency",
                                        HttpStatus.BAD_REQUEST,
                                        Map.of(
                                                        "sourceCurrency", sourceAccount.getCurrency(),
                                                        "destinationCurrency", destinationAccount.getCurrency()));
                }

                // Validate sufficient funds
                if (sourceAccount.getBalance().compareTo(tx.getAmount()) < 0) {
                        throw new InsufficientFundsException(
                                        sourceAccount.getId(),
                                        sourceAccount.getBalance(),
                                        tx.getAmount());
                }

                // Perform transfer
                sourceAccount.debit(tx.getAmount());
                destinationAccount.credit(tx.getAmount());

                // Create mirror transaction for destination account
                Transaction mirrorTx = Transaction.builder()
                                .account(destinationAccount)
                                .amount(tx.getAmount())
                                .currency(tx.getCurrency())
                                .type(TransactionType.TRANSFER)
                                .direction(TransactionDirection.INBOUND)
                                .status(Transaction.TransactionStatus.COMPLETED)
                                .description("Transfer from " + sourceAccount.getAccountNumber())
                                .referenceNumber(tx.getReferenceNumber())
                                .linkedTransaction(tx)
                                .timestamp(Instant.now())
                                .build();

                transactionRepo.save(mirrorTx);
                tx.setLinkedTransaction(mirrorTx);
                tx.setStatus(TransactionStatus.COMPLETED);
        }

        @Override
        public void cancelTransaction(String transactionId, UUID userId) {
                Transaction transaction = transactionRepo.findByIdAndUserId(transactionId, userId)
                                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

                if (transaction.getStatus() == TransactionStatus.CANCELLED) {
                        throw new BankingException("ALREADY_CANCELLED", "Transaction already cancelled",
                                        HttpStatus.CONFLICT);
                }

                if (transaction.getStatus() != TransactionStatus.PENDING) {
                        throw new BankingException("CANNOT_CANCEL", "Only pending transactions can be cancelled",
                                        HttpStatus.BAD_REQUEST);
                }

                transaction.setStatus(TransactionStatus.CANCELLED);
                transactionRepo.save(transaction);

                User user = transaction.getAccount().getUser(); // assuming BankAccount → User relationship exists
                auditService.logTransactionCancellation(transaction, user);
        }

        @Override
        public List<String> getAccountNumbersForTransactions(List<String> transactionIds) {
                if (transactionIds == null || transactionIds.isEmpty()) {
                        return Collections.emptyList();
                }

                return transactionRepo.findAllById(transactionIds)
                                .stream()
                                .map(transaction -> transaction.getAccount().getAccountNumber())
                                .distinct()
                                .toList();
        }

}