package com.mgaye.banking_backend.dto.request;

// UpdateSecuritySettingsRequest.java
public record UpdateSecuritySettingsRequest(
        boolean enableTwoFactor,
        boolean enableLoginAlerts) {
}