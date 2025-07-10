
package com.mgaye.banking_backend.repository;

import com.mgaye.banking_backend.model.TransactionAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionAuditRepository extends JpaRepository<TransactionAudit, String> {

    @Query("SELECT a FROM TransactionAudit a WHERE a.transaction.id = :transactionId ORDER BY a.createdAt DESC")
    List<TransactionAudit> findByTransactionId(String transactionId);

    @Query("SELECT a FROM TransactionAudit a WHERE a.transaction.account.id = :accountId AND a.action = 'STATUS_CHANGE'")
    List<TransactionAudit> findStatusChangesByAccount(String accountId);
}