package com.mgaye.banking_backend.dto.request;

import com.mgaye.banking_backend.model.Beneficiary.BeneficiaryType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BeneficiaryCreateRequest(
                @NotBlank String name,
                @NotBlank @Size(min = 8, max = 34) String accountNumber,
                @Size(min = 8, max = 20) String routingNumber,
                @NotBlank @Size(min = 3, max = 3) String currency,
                BeneficiaryType type,
                @NotBlank String bankName,
                String bankAddress,
                @Email String email,
                BeneficiaryLimitsRequest limits) {
}
