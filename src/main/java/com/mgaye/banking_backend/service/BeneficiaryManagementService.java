package com.mgaye.banking_backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.request.BeneficiaryCreateRequest;
import com.mgaye.banking_backend.model.AccountBeneficiary;
import com.mgaye.banking_backend.model.Beneficiary;
import com.mgaye.banking_backend.model.Beneficiary.BeneficiaryType;
import com.mgaye.banking_backend.repository.AccountBeneficiaryRepository;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.BeneficiaryRepository;
import com.mgaye.banking_backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeneficiaryManagementService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final AccountBeneficiaryRepository accountBeneficiaryRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    // Add a beneficiary to both systems
    @Transactional
    public Beneficiary addBeneficiary(UUID userId, BeneficiaryCreateRequest request) {
        // Create the global beneficiary record
        Beneficiary beneficiary = Beneficiary.builder()
                .user(userRepository.getReferenceById(userId.toString()))
                .name(request.name())
                .accountNumber(request.accountNumber())
                .routingNumber(request.routingNumber())
                .currency(request.currency())
                .type(request.type())
                .bankName(request.bankName())
                .build();

        beneficiaryRepository.save(beneficiary);

        // If internal, add to all the user's accounts
        if (beneficiary.getType() == BeneficiaryType.INTERNAL) {
            bankAccountRepository.findByUserId(userId).forEach(account -> {
                if (!accountBeneficiaryRepository.existsByAccountAndAccountNumber(
                        account, beneficiary.getAccountNumber())) {

                    AccountBeneficiary ab = AccountBeneficiary.builder()
                            .account(account)
                            .beneficiaryName(beneficiary.getName())
                            .accountNumber(beneficiary.getAccountNumber())
                            .build();

                    accountBeneficiaryRepository.save(ab);
                }
            });
        }

        return beneficiary;
    }

    // Get all beneficiaries for an account (combines both types)
    public List<AccountBeneficiary> getAccountBeneficiaries(UUID accountId) {
        List<AccountBeneficiary> accountBeneficiaries = accountBeneficiaryRepository.findByAccountId(accountId);

        // You could enrich with global beneficiary data here
        return accountBeneficiaries;
    }
}