package com.mgaye.banking_backend.dto.response;

import java.math.BigDecimal;

// public record LoanCalculationResult(
//         BigDecimal principalAmount,
//         Integer termMonths,
//         BigDecimal interestRate, // Annual percentage rate
//         BigDecimal monthlyPayment,
//         BigDecimal totalInterest) {
//     public String getFormattedInterestRate() {
//         return String.format("%.2f%%", interestRate);
//     }
// }

public record LoanCalculationResult(
        BigDecimal principalAmount,
        Integer termMonths,
        BigDecimal annualInterestRate, // Changed from interestRate to annualInterestRate
        BigDecimal monthlyPayment,
        BigDecimal totalInterest) {
    public String getFormattedInterestRate() {
        return String.format("%.2f%%", annualInterestRate);
    }

    public BigDecimal getTotalPayment() {
        return principalAmount.add(totalInterest);
    }
}