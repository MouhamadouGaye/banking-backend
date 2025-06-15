package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

// // TransferRequest.java
// public record TransferRequest(
//         @NotBlank String fromAccountId,
//         @NotBlank String toAccountNumber,
//         @Positive BigDecimal amount,
//         @NotBlank String currency,
//         String note) {
// }

public record TransferRequest(
                String fromAccountNumber,
                String toAccountNumber,
                BigDecimal amount,
                String currency,
                String referenceId) {
        public TransferRequest {
                Objects.requireNonNull(amount, "Amount cannot be null");
                Objects.requireNonNull(currency, "Currency cannot be null");
        }
}