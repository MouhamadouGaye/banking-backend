package com.mgaye.banking_backend.event;

import java.util.Map;

// event/NotificationEvent.java
public record NotificationEvent(
        String userId,
        NotificationType type,
        String message,
        Map<String, Object> metadata) {
    public enum NotificationType {
        TRANSACTION, SECURITY, PROMOTION, PAYMENT_DUE
    }
}