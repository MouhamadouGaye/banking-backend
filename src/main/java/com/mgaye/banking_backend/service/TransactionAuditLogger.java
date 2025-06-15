package com.mgaye.banking_backend.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.TransactionAudit;
import com.mgaye.banking_backend.repository.TransactionAuditRepository;

import lombok.*;
import lombok.RequiredArgsConstructor;

// service/TransactionAuditLogger.java
@Service
@RequiredArgsConstructor
public class TransactionAuditLogger {
    private final TransactionAuditRepository auditRepository;

    @Async
    public void log(Transaction transaction) {
        TransactionAudit audit = TransactionAudit.builder()
                .transaction(transaction)
                .action(TransactionAudit.AuditAction.CREATE)
                .changedBy("SYSTEM")
                .build();

        auditRepository.save(audit);
    }
}