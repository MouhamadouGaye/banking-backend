package com.mgaye.banking_backend.dto;

// NotificationPreferencesDto.java
public record NotificationPreferencesDto(
        boolean email,
        boolean sms,
        boolean push) {
}