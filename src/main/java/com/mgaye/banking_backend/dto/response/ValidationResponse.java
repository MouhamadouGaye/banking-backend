package com.mgaye.banking_backend.dto.response;

public record ValidationResponse(
        boolean isValid,
        String accountHolderName,
        String bankName,
        String message) {
}