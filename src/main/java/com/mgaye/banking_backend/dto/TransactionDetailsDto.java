package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.transaction.TransactionStatus;

import com.mgaye.banking_backend.model.BankAccount.AccountType;
import com.mgaye.banking_backend.model.Transaction.TransactionDirection;
import com.mgaye.banking_backend.model.Transaction.TransactionType;

public record TransactionDetailsDto(
        String id,
        String accountNumber,
        AccountType accountType,
        BigDecimal amount,
        String currency,
        TransactionType type,
        TransactionDirection direction,
        TransactionStatus status,
        String description,
        String referenceId,
        Instant timestamp) {
}