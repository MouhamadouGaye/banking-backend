package com.mgaye.banking_backend.service;

import java.time.Instant;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.AuditLog;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// service/AuditService.java
@Service
@RequiredArgsConstructor
public class AuditService {
    private final auditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Async
    public void logAction(String userId, AuditAction action, String entityType, String entityId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId));

        AuditLog log = AuditLog.builder()
                .user(user)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .timestamp(Instant.now())
                .build();

        auditLogRepository.save(log);
    }

    public enum AuditAction {
        CREATE, UPDATE, DELETE, ACCESS, APPROVE, REJECT
    }
}