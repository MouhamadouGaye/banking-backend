package com.mgaye.banking_backend.dto;

public record TransactionItem(
        String id,
        Instant date,
        TransactionType type,
        BigDecimal amount,
        String description,
        String accountNumber,
        TransactionStatus status) {
}