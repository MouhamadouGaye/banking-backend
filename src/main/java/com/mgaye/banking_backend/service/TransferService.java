package com.mgaye.banking_backend.service;

import java.math.BigDecimal;
import java.util.List;

import com.mgaye.banking_backend.dto.TransferResult;
import com.mgaye.banking_backend.dto.request.TransactionRequest;
import com.mgaye.banking_backend.exception.AccountNotFoundException;
import com.mgaye.banking_backend.exception.InsufficientFundsException;
import com.mgaye.banking_backend.exception.TransferValidationException;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Transaction;

public interface TransferService {
    /**
     * Executes a transfer between accounts using your existing TransactionRequest
     * format
     */
    Transaction executeTransfer(TransactionRequest request)
            throws AccountNotFoundException, InsufficientFundsException, TransferValidationException;

    /**
     * Batch transfer execution
     */
    List<Transaction> executeBatchTransfers(List<TransactionRequest> requests);
}