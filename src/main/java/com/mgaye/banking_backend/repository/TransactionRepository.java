package com.mgaye.banking_backend.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mgaye.banking_backend.dto.TransactionSummaryDto;
import com.mgaye.banking_backend.model.Transaction;

import jakarta.persistence.LockModeType;

// repository/TransactionRepository.java
public interface TransactionRepository
        extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId ORDER BY t.timestamp DESC")
    Page<Transaction> findByAccountId(@Param("accountId") String accountId, Pageable pageable);

    @Query("""
            SELECT new com.mgaye.banking_backend.dto.TransactionSummaryDto(
                DATE(t.timestamp),
                COUNT(t),
                SUM(t.amount))
            FROM Transaction t
            WHERE t.account.id = :accountId
            AND t.timestamp BETWEEN :start AND :end
            GROUP BY DATE(t.timestamp)
            """)
    List<TransactionSummaryDto> getDailySummary(
            @Param("accountId") String accountId,
            @Param("start") Instant start,
            @Param("end") Instant end);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Transaction t WHERE t.id = :id")
    Optional<Transaction> findByIdForUpdate(@Param("id") String id);

    @Modifying
    @Query("UPDATE Transaction t SET t.status = 'FAILED' WHERE t.id IN :ids AND t.status = 'PENDING'")
    void markFailedTransactions(@Param("ids") List<String> transactionIds);

    Optional<Transaction> findByIdAndUserId(String id, String userId);

}