package com.mgaye.banking_backend.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.mgaye.banking_backend.model.Beneficiary;
import com.mgaye.banking_backend.model.Beneficiary.BeneficiaryType;

public record BeneficiaryResponse(
        UUID id,
        String name,
        String maskedAccountNumber,
        String bankName,
        String currency,
        BeneficiaryType type,
        boolean verified,
        Instant createdAt) {
    public BeneficiaryResponse(Beneficiary beneficiary) {
        this(
                beneficiary.getId(),
                beneficiary.getName(),
                "****" + beneficiary.getAccountNumber().substring(beneficiary.getAccountNumber().length() - 4),
                beneficiary.getBankName(),
                beneficiary.getCurrency(),
                beneficiary.getType(),
                beneficiary.isVerified(),
                beneficiary.getCreatedAt());
    }
}
