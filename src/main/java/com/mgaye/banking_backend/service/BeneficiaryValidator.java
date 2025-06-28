package com.mgaye.banking_backend.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.request.BeneficiaryCreateRequest;
import com.mgaye.banking_backend.dto.request.BeneficiaryLimitsRequest;
import com.mgaye.banking_backend.dto.response.ValidationResponse;
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
            @requireNonNull(request.routingNumber(), 
                "Routing number required for international transfers");
            
            ValidationResponse validation = bankCodeService.getDetailedValidation(
                request.getAccountNumber(),
                request.getRoutingNumber()
            );
            
            if (!validation.isValid()) {
                throw new ValidationException(
                    String.format("Invalid bank details: %s", validation.message()));
            }
            
            if (!request.getName().equalsIgnoreCase(validation.accountHolderName())) {
                throw new ValidationException(
                    "Beneficiary name doesn't match account holder name");
            }
        }
    }

    public ValidationResponse validateBeneficiaryDetails(Beneficiary beneficiary) {
        return beneficiary.getType() == BeneficiaryType.INTERNAL
                ? new ValidationResponse(true, beneficiary.getName(), "Internal Transfer", "Valid")
                : bankCodeService.getDetailedValidation(
                        beneficiary.getAccountNumber(),
                        beneficiary.getRoutingNumber());
    }

    // ... rest of the methods unchanged
}