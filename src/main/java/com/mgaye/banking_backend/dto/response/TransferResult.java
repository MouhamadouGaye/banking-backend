package com.mgaye.banking_backend.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferResult(
        String id,
        String status, // "COMPLETED", "FAILED"
        String message,
        String fromAccount,
        String toAccount,
        BigDecimal amount,
        String currency,
        Instant processedAt,
        String referenceId) {
}