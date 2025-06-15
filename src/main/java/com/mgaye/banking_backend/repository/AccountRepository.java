package com.mgaye.banking_backend.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mgaye.banking_backend.model.BankAccount;

import jakarta.persistence.LockModeType;

// repository/AccountRepository.java
public interface AccountRepository extends JpaRepository<BankAccount, String> {

    @EntityGraph(attributePaths = { "user" })
    Optional<BankAccount> findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM BankAccount a WHERE a.user.id = :userId AND a.status = 'ACTIVE'")
    List<BankAccount> findActiveAccountsByUser(@Param("userId") String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM BankAccount a WHERE a.accountNumber = :accountNumber")
    Optional<BankAccount> findByAccountNumberForUpdate(@Param("accountNumber") String accountNumber);

    @Modifying
    @Query("UPDATE BankAccount a SET a.balance = a.balance + :amount WHERE a.id = :accountId")
    void updateBalance(
            @Param("accountId") String accountId,
            @Param("amount") BigDecimal amount);

    boolean existsByAccountNumberAndUser_Id(String accountNumber, String userId);
}