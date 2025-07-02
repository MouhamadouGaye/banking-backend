package com.mgaye.banking_backend.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import com.mgaye.banking_backend.model.Loan;
import com.mgaye.banking_backend.model.Loan.LoanStatus;
import com.mgaye.banking_backend.model.Loan.LoanType;

public record LoanApplicationResponse(
        UUID loanId,
        LoanType type,
        BigDecimal principalAmount,
        String currency,
        LoanStatus status,
        Integer termMonths,
        Instant applicationDate,
        BigDecimal totalAmount,
        BigDecimal interestRate,
        BigDecimal monthlyPayment) {
    public LoanApplicationResponse(Loan loan) {
        this(
                loan.getId(),
                loan.getType(),
                loan.getPrincipalAmount(),
                loan.getCurrency(),
                loan.getStatus(),
                loan.getTermMonths(),
                loan.getApplicationDate(),
                loan.getPrincipalAmount().add(loan.getTotalInterest()),
                loan.getInterestRate(),
                loan.getMonthlyPayment());
    }
}