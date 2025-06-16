package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.exception.TokenRefreshException;
import com.mgaye.banking_backend.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);
    // ... other methods ...

    public RefreshToken createRefreshToken(String userId);

    public void deleteByUserId(String userId);
}