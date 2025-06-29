package com.mgaye.banking_backend.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.response.LoanCalculationResult;
import com.mgaye.banking_backend.model.LoanApplication;
import com.mgaye.banking_backend.model.PaymentSchedule;
import com.mgaye.banking_backend.service.LoanCalculatorService;

@Service
public class LoanCalculatorServiceImpl implements LoanCalculatorService {
    @Override
    public LoanCalculationResult calculate(BigDecimal principal, Integer termMonths, BigDecimal baseInterestRate) {
        // Input validation
        if (principal == null || termMonths == null || baseInterestRate == null) {
            throw new IllegalArgumentException("Input parameters cannot be null");
        }
        if (principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Principal amount must be positive");
        }
        if (termMonths <= 0) {
            throw new IllegalArgumentException("Loan term must be positive");
        }

        // Calculate monthly interest rate
        BigDecimal monthlyRate = baseInterestRate
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        // Calculate monthly payment using standard formula: P * r * (1+r)^n / ((1+r)^n
        // - 1)
        BigDecimal numerator = monthlyRate.multiply(
                BigDecimal.ONE.add(monthlyRate).pow(termMonths, MathContext.DECIMAL64));
        BigDecimal denominator = BigDecimal.ONE.add(monthlyRate)
                .pow(termMonths, MathContext.DECIMAL64)
                .subtract(BigDecimal.ONE);

        BigDecimal monthlyPayment = principal.multiply(numerator)
                .divide(denominator, 2, RoundingMode.HALF_UP);

        // Calculate total interest
        BigDecimal totalInterest = monthlyPayment.multiply(BigDecimal.valueOf(termMonths))
                .subtract(principal);

        return new LoanCalculationResult(
                principal,
                termMonths,
                baseInterestRate,
                monthlyPayment,
                totalInterest);
    }

    private BigDecimal calculateRiskAdjustment(BigDecimal amount) {
        // Risk increases with loan amount
        if (amount.compareTo(new BigDecimal("5000")) < 0)
            return BigDecimal.ZERO;
        if (amount.compareTo(new BigDecimal("20000")) < 0)
            return new BigDecimal("0.5");
        return new BigDecimal("1.25");
    }

    private BigDecimal calculateTotalInterest(BigDecimal principal, BigDecimal monthlyPayment, int termMonths) {
        return monthlyPayment.multiply(BigDecimal.valueOf(termMonths))
                .subtract(principal);
    }

}