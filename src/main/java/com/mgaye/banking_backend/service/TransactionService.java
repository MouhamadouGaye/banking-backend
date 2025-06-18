package com.mgaye.banking_backend.service;

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

    Transaction create(TransactionRequest request, User user);

    Transaction buildTransaction(TransactionRequest request, BankAccount account);

    Transaction getTransaction(String id);

    void processDoubleEntry(Transaction tx, BankAccount account);

    void processTransfer(Transaction tx);

    Transaction processTransaction(TransactionRequest request, User user);

    Transaction getTransactionForUser(String transactionId, String userId);

}