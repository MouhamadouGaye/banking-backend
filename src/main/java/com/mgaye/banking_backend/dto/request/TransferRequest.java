package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;

import java.util.Objects;

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
