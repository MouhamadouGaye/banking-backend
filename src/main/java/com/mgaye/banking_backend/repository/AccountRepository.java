package com.mgaye.banking_backend.repository;

public class AccountRepository {

}

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