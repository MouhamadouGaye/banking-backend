package com.mgaye.banking_backend.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// dto/request/CardIssuanceRequest.java
public record CardIssuanceRequest(
        @NotBlank String userId,
        @NotBlank String accountId,
        @NotBlank String username,

        @NotNull CardType cardType,
        @NotNull CardDesign design,
        @NotBlank @Pattern(regexp = "[A-Z]{2}") String currency,
        boolean virtualCard,
        @NotNull @Future LocalDate expiryDate) {
    public enum CardType {
        DEBIT, CREDIT, PREPAID
    }

    public enum CardDesign {
        STANDARD, GOLD, PLATINUM, CUSTOM
    }
}