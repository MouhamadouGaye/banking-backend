// package com.mgaye.banking_backend.service;

// import java.util.List;

// import com.mgaye.banking_backend.dto.TransferResult;
// import com.mgaye.banking_backend.dto.request.TransferRequest;

// // service/BatchTransactionService.java
// public interface BatchTransactionService {
//     BatchResult processBatchTransfers(List<TransferRequest> requests)
//     TransferResult processSingleTransfer(TransferRequest request);
// }

// package com.mgaye.banking_backend.service;

// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.TransactionStatus;
// import org.springframework.transaction.annotation.Transactional;

// import com.mgaye.banking_backend.dto.TransferResult;
// import com.mgaye.banking_backend.dto.request.TransferRequest;
// import com.mgaye.banking_backend.model.BankAccount;
// import com.mgaye.banking_backend.model.BatchResult;
// import com.mgaye.banking_backend.model.Transaction;
// import com.mgaye.banking_backend.model.Transaction.TransactionType;
// import com.mgaye.banking_backend.repository.BankAccountRepository;
// import com.mgaye.banking_backend.repository.TransactionRepository;

// import java.math.BigDecimal;
// import java.time.Instant;
// import java.util.List;
// import java.util.Map;
// import java.util.Objects;
// import java.util.UUID;
// import java.util.concurrent.CompletableFuture;
// import java.util.concurrent.Executor;

// @Service
// @RequiredArgsConstructor
// public class BatchTransferService {

//         private final BankAccountRepository accountRepo;
//         private final TransactionRepository txRepo;
//         private final Executor batchExecutor;

//         @Transactional
//         public BatchResult processBatchTransfers(List<TransferRequest> requests) {
//                 List<CompletableFuture<TransferResult>> futures = requests.stream()
//                                 .map(req -> CompletableFuture.supplyAsync(
//                                                 () -> processSingleTransfer(req),
//                                                 batchExecutor))
//                                 .toList();

//                 List<TransferResult> results = futures.stream()
//                                 .map(CompletableFuture::join)
//                                 .toList();

//                 return new BatchResult(
//                                 results,
//                                 UUID.randomUUID().toString(),
//                                 Instant.now());
//         }

//         private TransferResult processSingleTransfer(TransferRequest request) {
//                 try {
//                         // Lock accounts for update
//                         BankAccount fromAccount = accountRepo.findByAccountNumberForUpdate(request.fromAccountNumber())
//                                         .orElseThrow(() -> new IllegalArgumentException("From account not found"));
//                         BankAccount toAccount = accountRepo.findByAccountNumberForUpdate(request.toAccountNumber())
//                                         .orElseThrow(() -> new IllegalArgumentException("To account not found"));

//                         // Validate currencies match
//                         if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
//                                 return createFailedResult(request, "CURRENCY_MISMATCH");
//                         }

//                         // Check sufficient funds
//                         if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
//                                 return createFailedResult(request, "INSUFFICIENT_FUNDS");
//                         }

//                         // Process transfer
//                         fromAccount.setBalance(fromAccount.getBalance().subtract(request.amount()));
//                         toAccount.setBalance(toAccount.getBalance().add(request.amount()));

//                         // Create transactions
//                         Transaction debit = createTransaction(
//                                         fromAccount,
//                                         request.amount().negate(),
//                                         "DEBIT",
//                                         request.reference());
//                         Transaction credit = createTransaction(
//                                         toAccount,
//                                         request.amount(),
//                                         "CREDIT",
//                                         request.reference());

//                         // Save changes
//                         accountRepo.saveAll(List.of(fromAccount, toAccount));
//                         List<Transaction> savedTx = txRepo.saveAll(List.of(debit, credit));

//                         return new TransferResult(
//                                         savedTx.get(0).getId().toString(),
//                                         request.fromAccountNumber(),
//                                         request.toAccountNumber(),
//                                         request.amount(),
//                                         fromAccount.getCurrency(),
//                                         "COMPLETED",
//                                         Instant.now(),
//                                         request.reference());
//                 } catch (Exception e) {
//                         return createFailedResult(request, "FAILED: " + e.getMessage());
//                 }
//         }

//         private TransferResult createFailedResult(TransferRequest request, String status) {
//                 return new TransferResult(
//                                 null,
//                                 request.fromAccountNumber(),
//                                 request.toAccountNumber(),
//                                 request.amount(),
//                                 null, // Currency unknown in failure cases
//                                 status,
//                                 Instant.now(),
//                                 request.reference());
//         }

//         private Transaction createTransaction(BankAccount account, BigDecimal amount,
//                         String type, String reference) {
//                 return Transaction.builder()
//                                 .account(account)
//                                 .amount(amount)
//                                 .type(type)
//                                 .currency(account.getCurrency())
//                                 .description("Batch transfer - " + reference)
//                                 .status("PROCESSED")
//                                 .referenceNumber(reference)
//                                 .timestamp(Instant.now())
//                                 .build();
//         }
// }

package com.mgaye.banking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.dto.TransferResult;
import com.mgaye.banking_backend.dto.request.TransferRequest;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.BatchResult;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.TransactionFee;
import com.mgaye.banking_backend.model.Transaction.TransactionType;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class BatchTransferService {

        private final BankAccountRepository accountRepo;
        private final TransactionRepository txRepo;
        private final Executor batchExecutor;

        @Transactional
        public BatchResult processBatchTransfers(List<TransferRequest> requests) {
                List<CompletableFuture<TransferResult>> futures = requests.stream()
                                .map(req -> CompletableFuture.supplyAsync(
                                                () -> processSingleTransfer(req),
                                                batchExecutor))
                                .toList();

                List<TransferResult> results = futures.stream()
                                .map(CompletableFuture::join)
                                .toList();

                return new BatchResult(
                                results,
                                UUID.randomUUID().toString(),
                                Instant.now());
        }

        private TransferResult processSingleTransfer(TransferRequest request) {
                try {
                        // Lock accounts for update with currency check
                        BankAccount fromAccount = accountRepo.findByAccountNumberWithCurrencyForUpdate(
                                        request.fromAccountNumber(), request.currency())
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "From account not found or currency mismatch"));

                        BankAccount toAccount = accountRepo.findByAccountNumberWithCurrencyForUpdate(
                                        request.toAccountNumber(), request.currency())
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "To account not found or currency mismatch"));

                        // Check sufficient funds
                        if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
                                return createFailedResult(request, "INSUFFICIENT_FUNDS");
                        }

                        // Process transfer
                        fromAccount.setBalance(fromAccount.getBalance().subtract(request.amount()));
                        toAccount.setBalance(toAccount.getBalance().add(request.amount()));

                        // Create transactions with fee calculation
                        Transaction debit = createTransaction(
                                        fromAccount,
                                        request.amount().negate(),
                                        TransactionType.TRANSFER,
                                        TransactionStatus.COMPLETED,
                                        request.referenceId(),
                                        "Outgoing transfer to " + request.toAccountNumber(),
                                        Map.of("batchTransfer", true));

                        Transaction credit = createTransaction(
                                        toAccount,
                                        request.amount(),
                                        TransactionType.TRANSFER,
                                        TransactionStatus.COMPLETED,
                                        request.referenceId(),
                                        "Incoming transfer from " + request.fromAccountNumber(),
                                        Map.of("batchTransfer", true));

                        // Apply fees if needed
                        applyTransferFee(debit, request);

                        // Save changes
                        accountRepo.saveAll(List.of(fromAccount, toAccount));
                        List<Transaction> savedTx = txRepo.saveAll(List.of(debit, credit));

                        return new TransferResult(
                                        savedTx.get(0).getId(),
                                        request.fromAccountNumber(),
                                        request.toAccountNumber(),
                                        request.amount(),
                                        request.currency(),
                                        "COMPLETED",
                                        Instant.now(),
                                        request.referenceId());
                } catch (Exception e) {
                        return createFailedResult(request, "FAILED: " + e.getMessage());
                }
        }

        private void applyTransferFee(Transaction transaction, TransferRequest request) {
                if (shouldApplyFee(request)) {
                        BigDecimal feeAmount = calculateFee(request.amount());
                        TransactionFee fee = TransactionFee.builder()
                                        .amount(feeAmount)
                                        .transaction(transaction)
                                        .feeType("TRANSFER_FEE")
                                        .build();
                        transaction.setFee(fee);
                }
        }

        private boolean shouldApplyFee(TransferRequest request) {
                // Your fee logic here
                return true;
        }

        private BigDecimal calculateFee(BigDecimal amount) {
                // Your fee calculation logic
                return amount.multiply(BigDecimal.valueOf(0.01)); // 1% fee example
        }

        private Transaction createTransaction(
                        BankAccount account,
                        BigDecimal amount,
                        TransactionType type,
                        TransactionStatus status,
                        String referenceId,
                        String description,
                        Map<String, Object> metadata) {
                return Transaction.builder()
                                .account(account)
                                .amount(amount)
                                .type(type)
                                .currency(account.getCurrency())
                                .status(status)
                                .timestamp(Instant.now())
                                .referenceId(referenceId)
                                .description(description)
                                .direction(amount.compareTo(BigDecimal.ZERO) > 0 ? TransactionDirection.CREDIT
                                                : TransactionDirection.DEBIT)
                                .metadata(metadata)
                                .build();
        }

        private TransferResult createFailedResult(TransferRequest request, String status) {
                return new TransferResult(
                                null,
                                request.fromAccountNumber(),
                                request.toAccountNumber(),
                                request.amount(),
                                request.currency(),
                                status,
                                Instant.now(),
                                request.referenceId());
        }
}