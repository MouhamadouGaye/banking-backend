package com.mgaye.banking_backend.dto.response;

import com.mgaye.banking_backend.dto.UserDto;

// AuthResponse.java
public record AuthResponse(
                String accessToken,
                String refreshToken,
                long expiresIn,
                UserDto user) {
}