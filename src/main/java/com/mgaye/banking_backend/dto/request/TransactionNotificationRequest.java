package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TransactionNotificationRequest(
        @NotBlank String transactionId) {
}