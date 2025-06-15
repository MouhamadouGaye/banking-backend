package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.mgaye.banking_backend.model.PaymentSchedule;

// dto/LoanCalculationResult.java
public record LoanCalculationResult(
        BigDecimal monthlyPayment,
        BigDecimal totalInterest,
        List<PaymentSchedule> amortizationSchedule) {
}
