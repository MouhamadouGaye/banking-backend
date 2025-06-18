package com.mgaye.banking_backend.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AdminUserResponse(
        UUID userId,
        String email,
        String firstName,
        String lastName,
        boolean active,
        boolean locked,
        Instant lastLogin,
        List<String> roles,
        Instant createdAt) {
}
