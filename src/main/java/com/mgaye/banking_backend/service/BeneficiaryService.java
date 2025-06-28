package com.mgaye.banking_backend.service;

import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.dto.request.BeneficiaryCreateRequest;
import com.mgaye.banking_backend.dto.request.BeneficiaryLimitsRequest;
import com.mgaye.banking_backend.dto.response.ValidationResponse;
import com.mgaye.banking_backend.model.Beneficiary;

public interface BeneficiaryService {
    Beneficiary addBeneficiary(String userId, BeneficiaryCreateRequest request);

    Beneficiary getById(UUID id);

    ValidationResponse validateBeneficiary(UUID beneficiaryId, String userId);

    List<Beneficiary> getUserBeneficiaries(String userId);

    void removeBeneficiary(UUID beneficiaryId, String userId);

    Beneficiary updateLimits(UUID beneficiaryId, String userId, BeneficiaryLimitsRequest limits);
}