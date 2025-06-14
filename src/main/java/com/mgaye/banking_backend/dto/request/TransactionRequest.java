package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

// TransactionRequest.java
public record TransactionRequest(
        @NotBlank String accountId,
        @NotBlank String type,
        @Positive BigDecimal amount,
        @NotBlank String currency,
        String description,
        String merchantId) {
}
