package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.dto.request.PushNotificationRequest;

public interface FCMService {
    void sendPushNotification(PushNotificationRequest request);
}