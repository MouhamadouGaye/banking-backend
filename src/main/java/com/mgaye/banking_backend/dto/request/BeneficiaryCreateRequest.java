package com.mgaye.banking_backend.dto.request;

import com.mgaye.banking_backend.model.Beneficiary.BeneficiaryType;

import jakarta.validation.constraints.NotBlank;

public record BeneficiaryCreateRequest(
        @NotBlank String name,
        @NotBlank String accountNumber,
        String routingNumber,
        @NotBlank String bankName,
        @NotBlank String currency,
        BeneficiaryType type,
        String email,
        BeneficiaryLimitsRequest limits) {
}