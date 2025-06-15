package com.mgaye.banking_backend.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mgaye.banking_backend.model.BankAccount;

import jakarta.persistence.LockModeType;

// BankAccountRepository.java
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    @EntityGraph(attributePaths = { "user" })
    Optional<BankAccount> findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM BankAccount a WHERE a.user.id = :userId AND a.status = 'ACTIVE'")
    List<BankAccount> findActiveAccountsByUserId(@Param("userId") String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM BankAccount a WHERE a.accountNumber = :accountNumber")
    Optional<BankAccount> findByAccountNumberForUpdate(@Param("accountNumber") String accountNumber);

    @Query("SELECT a.balance FROM BankAccount a WHERE a.id = :accountId")
    Optional<BigDecimal> getBalance(@Param("accountId") String accountId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM BankAccount a WHERE a.accountNumber = :accountNumber AND a.currency = :currency")
    Optional<BankAccount> findByAccountNumberWithCurrencyForUpdate(
            @Param("accountNumber") String accountNumber,
            @Param("currency") String currency);
}