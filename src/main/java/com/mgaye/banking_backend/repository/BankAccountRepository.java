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
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.BankAccount.AccountStatus;
import com.mgaye.banking_backend.model.BankAccount.AccountType;
import com.mgaye.banking_backend.model.User;

import jakarta.persistence.LockModeType;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

        @EntityGraph(attributePaths = { "user" })
        Optional<BankAccount> findByIdWithUser(String accountId);

        boolean existsByIdAndUser(String accountId, User user);

        // Standard queries
        List<BankAccount> findByUserId(UUID userId);

        @EntityGraph(attributePaths = { "user" })
        Optional<BankAccount> findById(UUID accountId);

        boolean existsByIdAndUserId(UUID accountId, String userId);

        @EntityGraph(attributePaths = { "user" })
        Optional<BankAccount> findByAccountNumber(String accountNumber);

        // // Security-sensitive queries
        // Optional<BankAccount> findByIdAndUserId(UUID accountId, String userId);

        BankAccount findByIdAndUserId(UUID accountId, String userId);

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

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT a FROM BankAccount a WHERE a.accountNumber = :accountNumber AND a.currency = :currency")
        Optional<BankAccount> findByAccountNumberWithCurrencyForUpdate(
                        @Param("accountNumber") String accountNumber,
                        @Param("currency") String currency);

        // Utility queries
        @Query("SELECT a.balance FROM BankAccount a WHERE a.id = :accountId")
        Optional<BigDecimal> getBalanceById(@Param("accountId") UUID accountId);

        @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
                        "FROM BankAccount a WHERE a.user.id = :userId AND a.accountType = :accountType")
        boolean existsByUserAndAccountType(
                        @Param("userId") String userId,
                        @Param("accountType") BankAccount.AccountType accountType);

        @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
                        "FROM Transaction t WHERE t.beneficiaryAccountNumber = :accountNumber " +
                        "AND t.status = 'PENDING'")
        boolean existsPendingTransactionsForBeneficiary(@Param("accountNumber") String accountNumber);

        List<BankAccount> findByAccountTypeInAndStatus(List<AccountType> accountTypes, AccountStatus status);

        // // OR QUERY
        // @Query("SELECT a FROM BankAccount a WHERE a.accountType IN :accountTypes AND
        // a.status = :status")
        // List<BankAccount> findByAccountTypeInAndStatus(
        // @Param("accountTypes") List<AccountType> accountTypes,
        // @Param("status") AccountStatus status);
}
