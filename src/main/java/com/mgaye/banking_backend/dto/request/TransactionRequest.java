package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;

import com.mgaye.banking_backend.model.TransactionDirection;
import com.mgaye.banking_backend.model.Transaction.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// TransactionRequest.java
public record TransactionRequest(
                @NotBlank String accountId,
                @Positive @NotNull BigDecimal amount,
                @NotBlank String currency,
                @NotNull TransactionType type,
                @NotNull TransactionDirection direction,
                String description,
                String referenceId,
                String merchantId) {
}