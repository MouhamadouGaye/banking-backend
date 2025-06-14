package com.mgaye.banking_backend.dto.response;

// TokenRefreshResponse.java
public record TokenRefreshResponse(
        String accessToken,
        String refreshToken) {
}