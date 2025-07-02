package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.transaction.TransactionStatus;

import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.Transaction.TransactionDirection;
import com.mgaye.banking_backend.model.Transaction.TransactionDirection;
import com.mgaye.banking_backend.model.Transaction.TransactionType;

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

// public record TransactionDto(
// String id,
// String accountId,
// TransactionType type,
// BigDecimal amount,
// String currency,
// String status,
// Instant timestamp,
// String description,
// TransactionDirection direction) {
// // public static TransactionDto fromEntity(Transaction transaction) {
// // return new TransactionDto(
// // transaction.getId(),
// // transaction.getAccount().getId().toString(),
// // transaction.getType(),
// // transaction.getAmount(),
// // transaction.getCurrency(),
// // transaction.getStatus().toString(),
// // transaction.getTimestamp(),
// // transaction.getDescription(),
// // transaction.getDirection());
// // }
// }
