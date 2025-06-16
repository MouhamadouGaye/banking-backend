package com.mgaye.banking_backend.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.AuditLog;
import com.mgaye.banking_backend.repository.AuditLogRepository;
import com.mgaye.banking_backend.service.AuditService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditLogRepository auditLogRepository;

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