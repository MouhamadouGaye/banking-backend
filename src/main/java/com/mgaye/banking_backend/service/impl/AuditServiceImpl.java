package com.mgaye.banking_backend.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.AuditLog;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.AuditLogRepository;
import com.mgaye.banking_backend.service.AuditService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditLogRepository auditLogRepository;

    @Override
    public void logTransaction(Transaction transaction, User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("transactionId", transaction.getId());
        params.put("amount", transaction.getAmount());
        params.put("currency", transaction.getCurrency());

        AuditLog log = AuditLog.builder()
                .userId(user.getId().toString())
                .action("TRANSACTION_PROCESSED")
                .principal(user.getFirstName() + user.getLastName())
                .eventType("TRANSACTION")
                .description(transaction.getDescription())
                .parameters(params)
                .timestamp(Instant.now())
                .status("SUCCESS")
                .build();

        auditLogRepository.save(log);
    }

    @Override
    public void recordAccountStatusChange(UUID accountId, String newStatus, String reason, User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", accountId.toString());
        params.put("newStatus", newStatus);

        AuditLog log = AuditLog.builder()
                .userId(user.getId().toString())
                .action("ACCOUNT_STATUS_CHANGE")
                .principal(user.getFirstName() + user.getLastName())
                .eventType("ACCOUNT")
                .description(reason)
                .parameters(params)
                .timestamp(Instant.now())
                .status("SUCCESS")
                .build();

        auditLogRepository.save(log);
    }

    @Override
    public void logTransactionCancellation(Transaction transaction, User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("transactionId", transaction.getId());
        params.put("amount", transaction.getAmount());
        params.put("currency", transaction.getCurrency());

        AuditLog log = AuditLog.builder()
                .userId(user.getId().toString())
                .action("TRANSACTION_CANCELLED")
                .principal(user.getFirstName() + user.getLastName())
                .eventType("TRANSACTION")
                .description("Transaction cancelled: " + transaction.getDescription())
                .parameters(params)
                .timestamp(Instant.now())
                .status("CANCELLED")
                .build();

        auditLogRepository.save(log);
    }

    @Override
    @Transactional
    public void logSecurityEvent(String userId, String eventType, String description) {
        AuditLog log = AuditLog.builder()
                .userId(userId)
                .eventType(eventType)
                .description(description)
                .timestamp(Instant.now())
                .build();
        auditLogRepository.save(log);
    }
}