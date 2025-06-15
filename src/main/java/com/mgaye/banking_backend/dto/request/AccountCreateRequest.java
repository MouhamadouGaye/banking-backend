package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.NotBlank;

// AccountCreateRequest.java
public record AccountCreateRequest(
        @NotBlank String userId,
        @NotBlank String accountType, // CHECKING/SAVINGS/BUSINESS
        @NotBlank String currency) {
}