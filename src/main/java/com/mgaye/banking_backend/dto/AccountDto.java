package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;

// AccountDto.java
public record AccountDto(
        String id,
        String accountNumber,
        String accountType,
        BigDecimal balance,
        String currency,
        String status,
        Instant createdAt) {
}