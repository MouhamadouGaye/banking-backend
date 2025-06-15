package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

// dto/TransferResult.java
public record TransferResult(
        String transactionId,
        String fromAccount,
        String toAccount,
        BigDecimal amount,
        String currency,
        String status,
        Instant processedAt,
        String referenceNumber) {
    public TransferResult {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        status = status != null ? status : "PENDING";
        processedAt = processedAt != null ? processedAt : Instant.now();
    }
}

// package com.yourpackage.dto;

// public record TransferResult(
// TransferRequest request,
// String status, // "COMPLETED", "INSUFFICIENT_FUNDS", "FAILED: reason"
// String transactionId,
// Instant processedAt
// ) {
// public TransferResult(TransferRequest request, String status) {
// this(request, status, null, Instant.now());
// }
// }