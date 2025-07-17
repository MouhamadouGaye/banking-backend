
package com.mgaye.banking_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.model.AuditLog;
import com.mgaye.banking_backend.model.AuditLogEntry;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {

    // List<AuditLog> findByUserId(UUID userId);

    // // Changed to use 'timestamp' field with Instant parameters
    // Page<AuditLog> findByTimestampBetween(Instant start, Instant end, Pageable
    // pageable);

    // // Changed to use 'action' field instead of non-existent 'actionType'
    // List<AuditLog> findByAction(String action);

    // List<AuditLogEntry> findByAccountIdOrderByTimestampDesc(UUID accountId);

    // // Changed to use 'eventType' and 'timestamp'
    // List<AuditLog> findByEventTypeAndUserIdOrderByTimestampDesc(String eventType,
    // UUID userId);

}