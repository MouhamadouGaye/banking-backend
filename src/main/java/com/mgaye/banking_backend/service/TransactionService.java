package com.mgaye.banking_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.controller.TransactionNotificationController;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.MerchantRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;

// TransactionService.java
public interface TransactionService {

    // Transaction create(TransactionRequest request, User user);

    // List<Transaction> getAccountTransactions(UUID accountId, LocalDate startDate,
    // LocalDate endDate);

    // void recordAccountStatusChange(UUID accountId, String newStatus, String
    // reason);

    // Transaction getTransaction(String id);

    // Transaction getTransactionForUser(String transactionId, String userId);

    // Transaction processTransaction(TransactionRequest request, User user);

    // Transaction buildTransaction(TransactionRequest request, BankAccount
    // account);

    // void processDoubleEntry(Transaction tx, BankAccount account);

    // void cancelTransaction(String transactionId, String userId);

    // Transaction processTransaction(Transaction transaction);

    Transaction create(TransactionRequest request, User user);

    List<Transaction> getAccountTransactions(UUID accountId, LocalDate startDate, LocalDate endDate);

    void recordAccountStatusChange(UUID accountId, String newStatus, String reason);

    Transaction getTransaction(String id);

    Transaction processTransaction(TransactionRequest request, User user);

    Transaction getTransactionForUser(String transactionId, String userId);
    // ... rest of the methods remain the same ...

    Transaction buildTransaction(TransactionRequest request, BankAccount account);

    void processDoubleEntry(Transaction tx, BankAccount account);

    void processTransfer(Transaction tx);

    void cancelTransaction(String transactionId, String userId);

    List<String> getAccountNumbersForTransactions(List<String> transactionIds);

}