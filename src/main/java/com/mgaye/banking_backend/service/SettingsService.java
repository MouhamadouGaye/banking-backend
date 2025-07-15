package com.mgaye.banking_backend.service;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.dto.NotificationPreferencesDto;
import com.mgaye.banking_backend.dto.SecuritySettingsDto;
import com.mgaye.banking_backend.dto.UserSettingsDto;
import com.mgaye.banking_backend.dto.request.UpdateSecuritySettingsRequest;

public interface SettingsService {

    // UserSettingsDto getUserSettings(Long userId);

    // UserSettingsDto updateUserSettings(Long userId, UserSettingsDto
    // userSettingsDto);

    // SecuritySettingsDto getSecuritySettings(Long userId);

    // SecuritySettingsDto updateSecuritySettings(Long userId,
    // UpdateSecuritySettingsRequest request);

    // void updateNotificationPreferences(Long userId, NotificationPreferencesDto
    // preferences);

    UserSettingsDto getUserSettings(UUID userId);

    UserSettingsDto updateUserSettings(UUID userId, UserSettingsDto userSettingsDto);

    SecuritySettingsDto getSecuritySettings(UUID userId);

    SecuritySettingsDto updateSecuritySettings(UUID userId, UpdateSecuritySettingsRequest request);
}
// public interface SettingsService {
// UserSettingsDto getUserSettings(String userId);
// UserSettingsDto updateUserSettings(String userId, UserSettingsDto
// userSettingsDto);
// SecuritySettingsDto getSecuritySettings(String userId); // Returns DTO, not
// Mapper
// SecuritySettingsDto updateSecuritySettings(String userId,
// UpdateSecuritySettingsRequest request);
// void updateNotificationPreferences(String userId, NotificationPreferencesDto
// preferences);
// }