package com.mgaye.banking_backend.dto;

// AuthResponse.java
public record AuthResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        UserDto user) {
}