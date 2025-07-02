package com.mgaye.banking_backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mgaye.banking_backend.model.AccountBeneficiary;
import com.mgaye.banking_backend.model.Beneficiary;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.Beneficiary.BeneficiaryType;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, UUID> {

    Optional<Beneficiary> findById(UUID id);

    boolean existsByUserAndAccountNumber(User user, String accountNumber);

    List<Beneficiary> findByUserAndType(User user, BeneficiaryType type);

    Optional<Beneficiary> findByIdAndUserId(UUID id, UUID userId);

    List<Beneficiary> findByUserId(UUID userId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Transaction t WHERE t.beneficiaryAccount = :accountNumber " +
            "AND t.status = 'PENDING'")
    boolean existsPendingTransactionsForBeneficiary(String accountNumber);
}