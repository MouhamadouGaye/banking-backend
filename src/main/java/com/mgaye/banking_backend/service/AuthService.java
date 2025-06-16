package com.mgaye.banking_backend.service;

import org.springframework.security.core.Authentication;

import com.mgaye.banking_backend.dto.response.AuthResponse;
import com.mgaye.banking_backend.dto.response.TokenRefreshResponse;
import com.mgaye.banking_backend.model.User;

public interface AuthService {
    Authentication authenticate(String email, String password);

    AuthResponse createAuthSession(User user);

    TokenRefreshResponse refreshToken(String refreshToken); // Add this method

}