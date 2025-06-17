package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.dto.NotificationPreferencesDto;
import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.model.NotificationPreferences;
import com.mgaye.banking_backend.model.UserSettings;

public interface NotificationService {
    void sendNotification(NotificationEvent event);

    boolean shouldSendNotification(NotificationEvent event, UserSettings settings);

    void updateNotificationPreferences(String userId, NotificationPreferencesDto preferencesDto);

    NotificationPreferencesDto getPreferences(String userId);

    void updateSinglePreference(String userId, String preferenceType, boolean enabled);

}
