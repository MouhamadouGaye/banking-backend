// NotificationServiceImpl.java

package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;
@Service
import javax.management.Notification;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.UserSettings;
import com.mgaye.banking_backend.repository.NotificationRepository;
import com.mgaye.banking_backend.repository.UserSettingsRepository;
import com.mgaye.banking_backend.service.NotificationDispatcher;


public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepo;
    private final UserSettingsRepository settingsRepo;
    private final NotificationDispatcher dispatcher;

    @Override
    @Async
    public void sendNotification(NotificationEvent event) {
        UserSettings settings = settingsRepo.findByUserId(event.userId())
                .orElseGet(() -> createDefaultSettings(event.userId()));

        if (shouldSendNotification(event, settings)) {
            Notification notification = createNotification(event);
            dispatcher.dispatch(notification, settings);
            notificationRepo.save(notification);
        }
    }

    private boolean shouldSendNotification(NotificationEvent event, UserSettings settings) {
        return switch (event.type()) {
            case SECURITY -> true; // Always send security notifications
            default -> settings.getNotificationPreferences().isEnabled(event.type());
        };
    }
}