// service/BatchTransactionServiceImpl.java
package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.request.TransferRequest;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;

public class BatchTransactionServiceImpl implements BatchTransactionService {
    private final BankAccountRepository accountRepo;
    private final TransactionRepository txRepo;
    private final ExecutorService batchExecutor = Executors.newFixedThreadPool(4);

    @Override
    @Transactional
    public BatchResult processBatchTransfers(List<TransferRequest> requests) {
        List<CompletableFuture<TransferResult>> futures = requests.stream()
                .map(req -> CompletableFuture.supplyAsync(() -> processSingleTransfer(req), batchExecutor))
                .toList();

        return new BatchResult(
                futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }

    private TransferResult processSingleTransfer(TransferRequest request) {
        try {
            BankAccount fromAccount = accountRepo.findByAccountNumberForUpdate(request.fromAccountNumber());
            BankAccount toAccount = accountRepo.findByAccountNumberForUpdate(request.toAccountNumber());

            if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
                return new TransferResult(request, "INSUFFICIENT_FUNDS");
            }

            Transaction debit = createTransaction(fromAccount, request.amount().negate(), "DEBIT");
            Transaction credit = createTransaction(toAccount, request.amount(), "CREDIT");

            txRepo.saveAll(List.of(debit, credit));
            return new TransferResult(request, "COMPLETED");
        } catch (Exception e) {
            return new TransferResult(request, "FAILED: " + e.getMessage());
        }
    }
}