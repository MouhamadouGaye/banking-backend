package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.NotBlank;

// CardIssueRequest.java
public record CardIssueRequest(
        @NotBlank String userId,
        @NotBlank String accountId,
        @NotBlank String cardType, // DEBIT/CREDIT
        @NotBlank String provider // VISA/MASTERCARD/etc.
) {
}