package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.exception.TokenRefreshException;
import com.mgaye.banking_backend.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);

    // ... other methods ...
    RefreshToken createRefreshToken(UUID userId);

    void deleteByUserId(UUID userId);

}