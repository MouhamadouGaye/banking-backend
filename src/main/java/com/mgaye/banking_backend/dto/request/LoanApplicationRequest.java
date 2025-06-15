package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

// LoanApplicationRequest.java
public record LoanApplicationRequest(
        @NotBlank String userId,
        @Positive BigDecimal amountRequested,
        @Min(6) @Max(84) int termMonths,
        String purpose,
        @Positive BigDecimal income,
        String employmentStatus) {
}