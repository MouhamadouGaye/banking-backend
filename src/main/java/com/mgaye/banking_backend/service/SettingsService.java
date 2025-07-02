package com.mgaye.banking_backend.service;

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

    UserSettingsDto getUserSettings(String userId);

    UserSettingsDto updateUserSettings(String userId, UserSettingsDto userSettingsDto);

    SecuritySettingsDto getSecuritySettings(String userId);

    SecuritySettingsDto updateSecuritySettings(String userId, UpdateSecuritySettingsRequest request);
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