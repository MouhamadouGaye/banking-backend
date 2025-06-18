package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.PositiveOrZero;

public record BeneficiaryLimitsRequest(
        @PositiveOrZero BigDecimal maxTransactionAmount,
        @PositiveOrZero BigDecimal dailyLimit,
        List<String> allowedPurposes) {
}