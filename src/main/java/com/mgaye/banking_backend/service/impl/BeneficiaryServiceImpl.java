package com.mgaye.banking_backend.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.dto.request.BeneficiaryCreateRequest;
import com.mgaye.banking_backend.dto.request.BeneficiaryLimitsRequest;
import com.mgaye.banking_backend.dto.response.ValidationResponse;
import com.mgaye.banking_backend.exception.BusinessRuleException;
import com.mgaye.banking_backend.exception.ConflictException;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.exception.UserNotFoundException;
import com.mgaye.banking_backend.model.Beneficiary;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.Beneficiary.BeneficiaryType;
import com.mgaye.banking_backend.model.Transaction.TransactionStatus;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.BeneficiaryRepository;
import com.mgaye.banking_backend.repository.TransactionRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.service.BeneficiaryService;
import com.mgaye.banking_backend.service.BeneficiaryValidator;
import com.mgaye.banking_backend.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final BeneficiaryValidator beneficiaryValidator;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public Beneficiary addBeneficiary(UUID userId, BeneficiaryCreateRequest request) {
        // Validate request
        beneficiaryValidator.validateCreateRequest(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Check for duplicate beneficiary
        if (beneficiaryRepository.existsByUserAndAccountNumber(user, request.accountNumber())) {
            throw new ConflictException("Beneficiary with this account number already exists");
        }

        // Create new beneficiary
        Beneficiary beneficiary = Beneficiary.builder()
                .user(user)
                .name(request.name())
                .accountNumber(request.accountNumber())
                .routingNumber(request.routingNumber())
                .currency(request.currency())
                .type(request.type())
                .bankName(request.bankName())
                .bankAddress(request.bankAddress())
                .email(request.email())
                .verified(false)
                .limits(request.limits())
                .build();

        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);

        // Send verification notification for external beneficiaries
        if (beneficiary.getType() != BeneficiaryType.INTERNAL) {
            notificationService.sendBeneficiaryVerificationNotification(user, savedBeneficiary);
        }

        log.info("Added new beneficiary {} for user {}", savedBeneficiary.getId(), userId);
        return savedBeneficiary;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Beneficiary> getUserBeneficiaries(UUID userId) {
        return beneficiaryRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void removeBeneficiary(UUID beneficiaryId, UUID userId) {
        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUserId(beneficiaryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));

        // Check if beneficiary is used in any pending transactions
        if (beneficiaryRepository
                .existsPendingTransactionsForBeneficiary(beneficiary.getAccountNumber()))
        // if (transactionRepository.existsPendingTransactionsForBeneficiary(
        // beneficiary.getAccountNumber(),
        // TransactionStatus.PENDING))
        {
            throw new BusinessRuleException("Cannot delete beneficiary with pending transactions");
        }

        beneficiaryRepository.delete(beneficiary);
        log.info("Removed beneficiary {} for user {}", beneficiaryId, userId);
    }

    public Beneficiary getById(UUID id) {
        return beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public ValidationResponse validateBeneficiary(UUID beneficiaryId, UUID userId) {
        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUserId(beneficiaryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));

        return beneficiaryValidator.validateBeneficiaryDetails(beneficiary);
    }

    @Override
    @Transactional
    public Beneficiary updateLimits(UUID beneficiaryId, UUID userId, BeneficiaryLimitsRequest limits) {
        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUserId(beneficiaryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));

        // Validate new limits
        beneficiaryValidator.validateLimits(limits);

        // Update limits
        beneficiary.setLimits(limits);
        Beneficiary updatedBeneficiary = beneficiaryRepository.save(beneficiary);

        log.info("Updated limits for beneficiary {} for user {}", beneficiaryId, userId);
        return updatedBeneficiary;
    }

}