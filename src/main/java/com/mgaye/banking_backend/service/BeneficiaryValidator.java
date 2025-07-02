package com.mgaye.banking_backend.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.request.BeneficiaryCreateRequest;
import com.mgaye.banking_backend.dto.request.BeneficiaryLimitsRequest;
import com.mgaye.banking_backend.dto.response.ValidationResponse;
import com.mgaye.banking_backend.exception.ValidationException;
import com.mgaye.banking_backend.model.Beneficiary;
import com.mgaye.banking_backend.model.Beneficiary.BeneficiaryType;
import com.mgaye.banking_backend.repository.BeneficiaryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BeneficiaryValidator {

    private final BankCodeService bankCodeService;
    private final BeneficiaryRepository beneficiaryRepository;

    public void validateCreateRequest(BeneficiaryCreateRequest request) {
        if (request.type() == BeneficiaryType.INTERNATIONAL) {
            java.util.Objects.requireNonNull(request.routingNumber(),
                    "Routing number required for international transfers");

            ValidationResponse validation = bankCodeService.getDetailedValidation(
                    request.accountNumber(),
                    request.routingNumber());

            if (!validation.isValid()) {
                throw new ValidationException(
                        String.format("Invalid bank details: %s", validation.message()));
            }

            if (!request.name().equalsIgnoreCase(validation.accountHolderName())) {
                throw new ValidationException(
                        "Beneficiary name doesn't match account holder name");
            }
        }
    }

    public void validateLimits(BeneficiaryLimitsRequest limits) {
        if (limits.maxTransactionAmount().signum() < 0 ||
                limits.dailyLimit().signum() < 0) {
            throw new ValidationException("Limits cannot be negative");
        }
        if (limits.allowedPurposes() == null || limits.allowedPurposes().isEmpty()) {
            throw new ValidationException("Allowed purposes cannot be empty");
        }
    }

    public ValidationResponse validateBeneficiaryDetails(Beneficiary beneficiary) {
        return beneficiary.getType() == BeneficiaryType.INTERNAL
                ? new ValidationResponse(true, "Internal Transfer", beneficiary.getName(), "Valid internal transfer")
                : bankCodeService.getDetailedValidation(
                        beneficiary.getAccountNumber(),
                        beneficiary.getRoutingNumber());
    }

}