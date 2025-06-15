package com.mgaye.banking_backend.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.model.RefreshToken;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);
    // ... other methods ...

    public RefreshToken createRefreshToken(String userId);

    public void deleteByUserId(String userId);
}