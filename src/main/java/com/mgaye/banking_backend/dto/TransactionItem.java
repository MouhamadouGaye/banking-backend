package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.mgaye.banking_backend.model.Transaction.TransactionStatus;
import com.mgaye.banking_backend.model.Transaction.TransactionType;

public record TransactionItem(
                String id,
                Instant date,
                TransactionType type,
                BigDecimal amount,
                String description,
                String accountNumber,
                TransactionStatus status) {
}
