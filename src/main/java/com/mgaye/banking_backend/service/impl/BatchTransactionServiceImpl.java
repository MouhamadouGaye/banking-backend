// service/BatchTransactionServiceImpl.java
package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.mgaye.banking_backend.service.BatchTransactionService;

import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mgaye.banking_backend.dto.TransferResult;
import com.mgaye.banking_backend.dto.request.TransferRequest;
import com.mgaye.banking_backend.dto.response.BatchResult;
import com.mgaye.banking_backend.exception.AccountNotFoundException;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.Transaction.TransactionType;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;

import com.mgaye.banking_backend.service.BatchTransactionService;

@Slf4j
@Service
public class BatchTransactionServiceImpl implements BatchTransactionService {
    private final BankAccountRepository accountRepo;
    private final TransactionRepository txRepo;
    private final PlatformTransactionManager transactionManager;
    private final ExecutorService batchExecutor;

    public BatchTransactionServiceImpl(
            BankAccountRepository accountRepo,
            TransactionRepository txRepo,
            PlatformTransactionManager transactionManager) {
        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
        this.transactionManager = transactionManager;
        this.batchExecutor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors(),
                new ThreadFactory() {
                    private final AtomicInteger threadCount = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("batch-transfer-" + threadCount.incrementAndGet());
                        thread.setDaemon(true);
                        return thread;
                    }
                });
    }

    @Override
    @Transactional
    public BatchResult processBatchTransfers(List<TransferRequest> transfers) {
        List<CompletableFuture<TransferResult>> futures = transfers.stream()
                .map(req -> CompletableFuture.supplyAsync(
                        () -> executeInTransaction(() -> processSingleTransfer(req)),
                        batchExecutor))
                .toList();

        List<TransferResult> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        return new BatchResult(
                results,
                UUID.randomUUID().toString(),
                Instant.now(),
                results.stream().filter(t -> "COMPLETED".equals(t.status())).count(),
                results.stream().filter(t -> !"COMPLETED".equals(t.status())).count());
    }

    private TransferResult processSingleTransfer(TransferRequest request) {
        try {
            BankAccount fromAccount = accountRepo.findByAccountNumberForUpdate(request.fromAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException(request.fromAccountNumber()));

            BankAccount toAccount = accountRepo.findByAccountNumberForUpdate(request.toAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException(request.toAccountNumber()));

            if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
                return TransferResult.fromRequest(request, "INSUFFICIENT_FUNDS");
            }

            // Update balances
            fromAccount.setBalance(fromAccount.getBalance().subtract(request.amount()));
            toAccount.setBalance(toAccount.getBalance().add(request.amount()));

            // Create transactions
            Transaction debit = createTransaction(fromAccount, request.amount().negate(), TransactionType.DEBIT);
            Transaction credit = createTransaction(toAccount, request.amount(), TransactionType.CREDIT);

            // Save everything
            accountRepo.saveAll(List.of(fromAccount, toAccount));
            txRepo.saveAll(List.of(debit, credit));

            return TransferResult.fromRequest(request, "COMPLETED");
        } catch (Exception e) {
            log.error("Transfer failed between {} and {}: {}",
                    request.fromAccountNumber(),
                    request.toAccountNumber(),
                    e.getMessage());
            return TransferResult.fromRequest(request, "FAILED: " + e.getMessage());
        }
    }

    private Transaction createTransaction(BankAccount account, BigDecimal amount, TransactionType type) {
        return Transaction.builder()
                .account(account)
                .amount(amount)
                .type(type)
                .timestamp(Instant.now())
                .referenceNumber(UUID.randomUUID().toString())
                .build();
    }

    private <T> T executeInTransaction(Supplier<T> operation) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute(status -> operation.get());
    }

    @PreDestroy
    public void shutdownExecutor() {
        batchExecutor.shutdown();
        try {
            if (!batchExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                batchExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            batchExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}