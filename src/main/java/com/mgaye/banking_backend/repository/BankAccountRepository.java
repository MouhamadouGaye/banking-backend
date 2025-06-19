// package com.mgaye.banking_backend.repository;

// import java.math.BigDecimal;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// import org.springframework.data.jpa.repository.EntityGraph;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Lock;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

// import com.mgaye.banking_backend.model.BankAccount;
// import com.mgaye.banking_backend.model.User;

// import jakarta.persistence.LockModeType;

// // BankAccountRepository.java
// public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

//     List<BankAccount> findByUserId(String userId);

//     List<BankAccount> findByUser_id(String userId);

//     Optional<BankAccount> findById(String userId);

//     BankAccount findByIdAndUserId(String accountId, String userId);

//     Optional<BankAccount> findByIdAndUserId(UUID accountId, String userId);

//     BankAccount findByIdAndUser(String accountId, User user);

//     @EntityGraph(attributePaths = { "user" })
//     Optional<BankAccount> findByAccountNumber(String accountNumber);

//     @Query("SELECT a FROM BankAccount a WHERE a.user.id = :userId AND a.status = 'ACTIVE'")
//     List<BankAccount> findActiveAccountsByUserId(@Param("userId") String userId);

//     @Lock(LockModeType.PESSIMISTIC_WRITE)
//     @Query("SELECT a FROM BankAccount a WHERE a.accountNumber = :accountNumber")
//     Optional<BankAccount> findByAccountNumberForUpdate(@Param("accountNumber") String accountNumber);

//     @Query("SELECT a.balance FROM BankAccount a WHERE a.id = :accountId")
//     Optional<BigDecimal> getBalance(@Param("accountId") String accountId);

//     @Lock(LockModeType.PESSIMISTIC_WRITE)
//     @Query("SELECT a FROM BankAccount a WHERE a.accountNumber = :accountNumber AND a.currency = :currency")
//     Optional<BankAccount> findByAccountNumberWithCurrencyForUpdate(
//             @Param("accountNumber") String accountNumber,
//             @Param("currency") String currency);
// }

package com.mgaye.banking_backend.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.User;

import jakarta.persistence.LockModeType;

public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

    // Standard queries
    List<BankAccount> findByUserId(String userId);

    @EntityGraph(attributePaths = { "user" })
    Optional<BankAccount> findById(UUID accountId);

    @EntityGraph(attributePaths = { "user" })
    Optional<BankAccount> findByAccountNumber(String accountNumber);

    // Security-sensitive queries
    Optional<BankAccount> findByIdAndUserId(UUID accountId, String userId);

    @EntityGraph(attributePaths = { "user" })
    Optional<BankAccount> findByIdAndUser(UUID accountId, User user);

    // Specialized queries
    @Query("SELECT a FROM BankAccount a WHERE a.user.id = :userId AND a.status = 'ACTIVE'")
    List<BankAccount> findActiveAccountsByUserId(@Param("userId") String userId);

    // Locking queries
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM BankAccount a WHERE a.accountNumber = :accountNumber")
    Optional<BankAccount> findByAccountNumberForUpdate(@Param("accountNumber") String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM BankAccount a WHERE a.id = :accountId")
    Optional<BankAccount> findByIdForUpdate(@Param("accountId") UUID accountId);

    // Utility queries
    @Query("SELECT a.balance FROM BankAccount a WHERE a.id = :accountId")
    Optional<BigDecimal> getBalanceById(@Param("accountId") UUID accountId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM BankAccount a WHERE a.user.id = :userId AND a.accountType = :accountType")
    boolean existsByUserAndAccountType(
            @Param("userId") String userId,
            @Param("accountType") BankAccount.AccountType accountType);
}
