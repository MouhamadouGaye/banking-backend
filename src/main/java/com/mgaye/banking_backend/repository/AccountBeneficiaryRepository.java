package com.mgaye.banking_backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mgaye.banking_backend.model.AccountBeneficiary;
import com.mgaye.banking_backend.model.BankAccount;

public interface AccountBeneficiaryRepository extends JpaRepository<AccountBeneficiary, UUID> {

    // Find all beneficiaries for a specific account
    List<AccountBeneficiary> findByAccountId(UUID accountId);

    // Check if a beneficiary already exists for an account
    boolean existsByAccountAndAccountNumber(BankAccount account, String accountNumber);

    // Find by account and beneficiary account number
    Optional<AccountBeneficiary> findByAccountAndAccountNumber(BankAccount account, String accountNumber);

    // Count how many beneficiaries an account has
    long countByAccount(BankAccount account);
}