package com.mgaye.banking_backend.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.dto.TransferResult;

// public record BatchResult(

//         List<TransferResult> results,
//         Long successCount,
//         Long failureCount,
//         String batchId) {
//     public BatchResult(List<TransferResult> results) {
//         this(
//                 results,
//                 results.stream().filter(r -> r.status().startsWith("COMPLETED")).count(),
//                 results.stream().filter(r -> !r.status().startsWith("COMPLETED")).count(),
//                 java.util.UUID.randomUUID().toString());
//     }
// }
// new BatchResult(
// results,
// UUID.randomUUID().toString(),
// Instant.now());
// import java.util.UUID;

public record BatchResult(

        List<TransferResult> transfers,
        String batchId,
        Instant processedAt,
        Long successCount,
        Long failureCount) {
    public BatchResult(List<TransferResult> transfers, String batchId, Instant processedAt,
            Long successCount, Long failureCount) {
        this.transfers = transfers;
        this.batchId = batchId;
        this.processedAt = processedAt;
        this.successCount = transfers.stream().filter(t -> "COMPLETED".equals(t.status())).count();
        this.failureCount = transfers.stream().filter(t -> !"COMPLETED".equals(t.status())).count();
    }

}