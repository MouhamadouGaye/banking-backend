package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import com.mgaye.banking_backend.model.Loan.LoanType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// public record LoanApplicationRequest(
//         @NotNull LoanType type,
//         @Positive BigDecimal amount,
//         @Min(1) int termMonths,
//         String purpose) {}

public record LoanApplicationRequest(
        @NotNull LoanType loanType,
        @Positive BigDecimal amount,
        @NotBlank String currency,
        @Min(1) Integer termMonths,
        String type,
        String purpose,
        UUID linkedAccountId,
        LoanTermsRequest terms) {
}
