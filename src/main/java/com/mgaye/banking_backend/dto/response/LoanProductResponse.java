package com.mgaye.banking_backend.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.mgaye.banking_backend.model.Loan.LoanType;

public record LoanProductResponse(
        String productId,
        String name,
        String description,
        LoanType type,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        BigDecimal interestRate,
        Integer minTerm,
        Integer maxTerm,
        List<String> eligibilityCriteria) {
}