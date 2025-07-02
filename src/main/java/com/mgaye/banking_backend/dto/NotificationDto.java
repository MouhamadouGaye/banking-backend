package com.mgaye.banking_backend.dto;

import java.time.Instant;
import java.util.Map;

// NotificationDto.java
public record NotificationDto(
        String id,
        String type,
        String message,
        boolean read,
        Instant timestamp,
        Map<String, Object> metadata) {
}
