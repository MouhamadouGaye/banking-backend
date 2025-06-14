package com.mgaye.banking_backend.service;

import java.util.List;

// service/BatchTransactionService.java
public interface BatchTransactionService {
    BatchResult processBatchTransfers(List<TransferRequest> transfers);
}

// dto/BatchResult.java
public record BatchResult(
        List<TransferResult> results,
        int successCount,
        int failureCount) {
    public BatchResult(List<TransferResult> results) {
        this(
                results,
                (int) results.stream().filter(r -> r.status().equals("COMPLETED")).count(),
                results.size() - (int) results.stream().filter(r -> r.status().equals("COMPLETED")).count());
    }
}
