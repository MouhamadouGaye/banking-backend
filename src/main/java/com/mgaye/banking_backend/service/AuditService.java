package com.mgaye.banking_backend.service;

import java.time.Instant;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.response.TokenRefreshResponse;
import com.mgaye.banking_backend.model.AuditLog;
import com.mgaye.banking_backend.model.RefreshToken;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.security.jwt.JwtUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

// service/AuditService.java
@Service
@RequiredArgsConstructor
public class AuditService {
    private final auditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

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

    @Override
    @Transactional
    public TokenRefreshResponse refreshToken(String refreshToken) {
        // 1. Find and verify the old refresh token
        RefreshToken oldToken = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token not found"));
        refreshTokenService.verifyExpiration(oldToken);

        // 2. Create new refresh token (rotation)
        User user = oldToken.getUser();
        refreshTokenService.deleteByUserId(user.getId());
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());

        // 3. Generate new access token
        String newAccessToken = jwtUtils.generateTokenFromUsername(user.getEmail());

        return new TokenRefreshResponse(newAccessToken, newRefreshToken.getToken());
    }
}