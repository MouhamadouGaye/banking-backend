package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

// LoanDto.java
public record LoanDto(
        String id,
        BigDecimal principalAmount,
        BigDecimal interestRate,
        int termMonths,
        BigDecimal monthlyPayment,
        BigDecimal outstandingBalance,
        String status,
        LocalDate issuedDate,
        LocalDate dueDate) {
}
