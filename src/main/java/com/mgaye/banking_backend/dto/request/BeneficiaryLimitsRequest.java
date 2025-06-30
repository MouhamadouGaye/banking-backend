package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.mgaye.banking_backend.model.Beneficiary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Request DTO for setting limits on a beneficiary.
 * This record encapsulates the maximum transaction amount, daily limit,
 * and a list of allowed purposes for transactions to this beneficiary.
 */
public record BeneficiaryLimitsRequest(
        @PositiveOrZero BigDecimal maxTransactionAmount,
        @PositiveOrZero BigDecimal dailyLimit,
        List<@NotBlank String> allowedPurposes) {
    // public BeneficiaryLimitsRequest(
    // BigDecimal maxTransactionAmount,
    // BigDecimal dailyLimit,
    // List<String> allowedPurposes) {
    // if (maxTransactionAmount == null || maxTransactionAmount.signum() < 0) {
    // throw new IllegalArgumentException("Max transaction amount must be
    // non-negative");
    // }
    // if (dailyLimit == null || dailyLimit.signum() < 0) {
    // throw new IllegalArgumentException("Daily limit must be non-negative");
    // }
    // if (allowedPurposes == null || allowedPurposes.isEmpty()) {
    // throw new IllegalArgumentException("Allowed purposes cannot be empty");
    // }
    // this.maxTransactionAmount = maxTransactionAmount;
    // this.dailyLimit = dailyLimit;
    // this.allowedPurposes = allowedPurposes;
    // }

}