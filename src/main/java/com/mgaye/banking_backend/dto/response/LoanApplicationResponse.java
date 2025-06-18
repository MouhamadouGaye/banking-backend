package com.mgaye.banking_backend.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.mgaye.banking_backend.model.Loan.LoanStatus;
import com.mgaye.banking_backend.model.Loan.LoanType;

public record LoanApplicationResponse(
        UUID loanId,
        LoanType type,
        BigDecimal requestedAmount,
        String currency,
        Integer termMonths,
        LoanStatus status,
        Instant applicationDate) {
}
