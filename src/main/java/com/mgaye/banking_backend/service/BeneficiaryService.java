package com.mgaye.banking_backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.dto.request.BeneficiaryCreateRequest;
import com.mgaye.banking_backend.dto.request.BeneficiaryLimitsRequest;
import com.mgaye.banking_backend.dto.response.ValidationResponse;
import com.mgaye.banking_backend.model.Beneficiary;

public interface BeneficiaryService {
    // Beneficiary addBeneficiary(String userName, BeneficiaryCreateRequest
    // request);

    // Beneficiary getById(UUID id);

    // ValidationResponse validateBeneficiary(UUID beneficiaryId, String userName);

    // List<Beneficiary> getUserBeneficiaries(String userName);

    // void removeBeneficiary(UUID beneficiaryId, String userName);

    // Beneficiary updateLimits(UUID beneficiaryId, String userName,
    // BeneficiaryLimitsRequest limits);

    // --------------
    // Beneficiary addBeneficiary(UUID userId, BeneficiaryCreateRequest request);

    // List<Beneficiary> getUserBeneficiaries(UUID userId);

    // void removeBeneficiary(UUID beneficiaryId, UUID userId);

    // Beneficiary getById(UUID id);

    // ValidationResponse validateBeneficiary(UUID beneficiaryId, UUID userId);

    // Beneficiary updateLimits(UUID beneficiaryId, UUID userId,
    // BeneficiaryLimitsRequest limits);

    // -------------------

    Beneficiary addBeneficiary(UUID userId, BeneficiaryCreateRequest request);

    ValidationResponse validateBeneficiary(UUID beneficiaryId, UUID userId);

    List<Beneficiary> getUserBeneficiaries(UUID userId);

    void removeBeneficiary(UUID beneficiaryId, UUID userId);

    Beneficiary updateLimits(UUID beneficiaryId, UUID userId, BeneficiaryLimitsRequest limits);

    Beneficiary getById(UUID id);

}