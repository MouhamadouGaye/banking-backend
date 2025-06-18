package com.mgaye.banking_backend.dto.response;

import java.time.Instant;
import java.util.UUID;

public record SystemAlertResponse(
        UUID alertId,
        String type,
        String severity,
        String message,
        Instant triggeredAt,
        boolean resolved) {
}