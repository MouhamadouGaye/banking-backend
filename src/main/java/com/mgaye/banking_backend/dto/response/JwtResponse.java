package com.mgaye.banking_backend.dto.response;

import java.util.List;

public record JwtResponse(
        String token,
        String refreshToken,
        String id,
        String username,
        String email,
        List<String> roles) {
    // Builder pattern alternative
    public static JwtResponse of(String token, String refreshToken, String id,
            String username, String email, List<String> roles) {
        return new JwtResponse(token, refreshToken, id, username, email, roles);
    }
}