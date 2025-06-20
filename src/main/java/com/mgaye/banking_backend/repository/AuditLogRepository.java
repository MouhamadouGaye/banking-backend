// package com.mgaye.banking_backend.repository;

// import java.util.List;

// import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
// import org.springframework.data.domain.Page;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

// import com.mgaye.banking_backend.model.AuditLog;
// import com.mgaye.banking_backend.model.BankAccount.AccountStatus;
// import com.mgaye.banking_backend.model.BankAccount.AccountType;

// // repository/AuditLogRepository.java
// public interface AuditLogRepository extends JpaRepository<AuditLog, String> {

//     @Query("SELECT a FROM AuditLog a WHERE a.entityType = 'ACCOUNT' AND a.entityId IN " +
//             "(SELECT acc.id FROM BankAccount acc WHERE acc.accountType IN :types AND acc.status = :status)")
//     Page<AuditLog> findByAccountTypeInAndStatus(
//             @Param("types") List<AccountType> types,
//             @Param("status") AccountStatus status,
//             Pageable pageable);
// }

package com.mgaye.banking_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgaye.banking_backend.model.AuditLog;
import com.mgaye.banking_backend.model.AuditLogEntry;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUserId(Long userId);

    Page<AuditLog> findByActionTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<AuditLog> findByActionType(String actionType);

    List<AuditLogEntry> findByAccountIdOrderByTimestampDesc(long accountId);

    List<AuditLog> findByEventTypeAndUserIdOrderByTimestampDesc(String eventType, String userId);

}