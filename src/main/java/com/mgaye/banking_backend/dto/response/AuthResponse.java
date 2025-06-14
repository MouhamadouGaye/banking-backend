package com.mgaye.banking_backend.dto.response;

// AuthResponse.java
public record AuthResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        UserDto user) {
}