// AuditLogAspect.java
package com.mgaye.banking_backend.util;

import java.time.Instant;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Auditable;
import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.model.AuditLog;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {
    private final auditLogRepository auditRepo;

    @Around("@annotation(auditable)")
    public Object logAudit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        String action = auditable.action();
        Map<String, Object> params = getMethodParams(joinPoint);

        AuditLog log = AuditLog.builder()
                .action(action)
                .parameters(params)
                .timestamp(Instant.now())
                .build();

        try {
            Object result = joinPoint.proceed();
            log.setStatus("SUCCESS");
            return result;
        } catch (Exception e) {
            log.setStatus("FAILED")
                    .setErrorDetails(e.getMessage());
            throw e;
        } finally {
            auditRepo.save(log);
        }
    }
}