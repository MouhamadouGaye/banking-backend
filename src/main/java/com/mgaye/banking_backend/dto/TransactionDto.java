package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;

// TransactionDto.java
public record TransactionDto(
        String id,
        String accountId,
        String type,
        BigDecimal amount,
        String currency,
        String status,
        Instant timestamp,
        String description,
        String referenceId,
        MerchantDto merchant,
        String direction) {
}