package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

// TransferRequest.java
public record TransferRequest(
        @NotBlank String fromAccountId,
        @NotBlank String toAccountNumber,
        @Positive BigDecimal amount,
        @NotBlank String currency,
        String note) {
}