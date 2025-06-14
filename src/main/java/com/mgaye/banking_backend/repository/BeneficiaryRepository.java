package com.mgaye.banking_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mgaye.banking_backend.model.AccountBeneficiary;

// repository/BeneficiaryRepository.java
public interface BeneficiaryRepository extends JpaRepository<AccountBeneficiary, String> {
    List<AccountBeneficiary> findByAccountId(String accountId);
}