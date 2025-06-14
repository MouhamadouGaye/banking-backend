package com.mgaye.banking_backend.dto;

// UserSettingsDto.java
public record UserSettingsDto(
        String language,
        String theme,
        NotificationPreferencesDto notificationPreferences,
        String currency) {
}