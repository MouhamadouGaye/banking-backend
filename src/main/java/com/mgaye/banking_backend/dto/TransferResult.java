package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.mgaye.banking_backend.dto.request.TransferRequest;

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

    // Factory method to create from TransferRequest
    public static TransferResult fromRequest(TransferRequest request, String status) {
        return new TransferResult(
                UUID.randomUUID().toString(), // Generate new transaction ID
                request.fromAccountNumber(),
                request.toAccountNumber(),
                request.amount(),
                request.currency(),
                status,
                Instant.now(),
                request.referenceNumber());
    }
}
