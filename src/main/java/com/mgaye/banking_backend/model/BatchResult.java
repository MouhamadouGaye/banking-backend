package com.mgaye.banking_backend.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.dto.TransferResult;

// public record BatchResult(

//         List<TransferResult> results,
//         long successCount,
//         long failureCount,
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
//                 results,
//                 UUID.randomUUID().toString(),
//                 Instant.now());
// import java.util.UUID;

public record BatchResult(
        List<TransferResult> transfers,
        String batchId,
        Instant processedAt,
        long successCount,
        long failureCount) {
    public BatchResult(List<TransferResult> transfers, String batchId, Instant processedAt) {
        this(
                transfers,
                batchId,
                processedAt,
                transfers.stream().filter(t -> "COMPLETED".equals(t.status())).count(),
                transfers.stream().filter(t -> !"COMPLETED".equals(t.status())).count());
    }
}