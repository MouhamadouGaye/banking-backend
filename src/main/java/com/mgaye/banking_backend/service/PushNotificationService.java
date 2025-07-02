package com.mgaye.banking_backend.service;

public interface PushNotificationService {
    void sendPushNotification(String deviceToken, String title, String message);
}