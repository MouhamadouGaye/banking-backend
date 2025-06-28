package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.service.PushNotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PushNotificationServiceImpl implements PushNotificationService {
    private final fcmService fcmService;

    @Override
    public void sendPushNotification(String deviceToken, String title, String message) {
        if (deviceToken == null || deviceToken.isBlank()) {
            throw new IllegalArgumentException("Device token cannot be null or empty");
        }

        try {
            fcmService.sendPushNotification(
                    new PushNotificationRequest(deviceToken, title, message));
        } catch (Exception e) {
            throw new PushNotificationException("Failed to send push notification", e);
        }
    }
}