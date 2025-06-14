package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// LoginRequest.java
public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password,
        String deviceId,
        String ipAddress) {
}