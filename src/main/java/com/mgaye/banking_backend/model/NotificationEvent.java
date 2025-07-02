package com.mgaye.banking_backend.model;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationEvent {
    private String userId;
    private NotificationType type;
    private NotificationChannel channel;
    private String title;
    private String message;
    private Map<String, Object> metadata;

    public enum NotificationType {
        TRANSACTION, SECURITY_ALERT, MARKETING, SYSTEM
    }

    public enum NotificationChannel {
        EMAIL, PUSH, SMS
    }
}