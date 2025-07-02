package com.mgaye.banking_backend.service;

import java.math.BigDecimal;

import com.mgaye.banking_backend.dto.response.LoanCalculationResult;

public interface LoanCalculatorService {
    LoanCalculationResult calculate(BigDecimal principal, Integer termMonths, BigDecimal baseInterestRate);
}