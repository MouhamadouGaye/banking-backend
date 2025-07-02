package com.mgaye.banking_backend.dto.request;

import java.time.Instant;

public record SmsSendRequest(
        String phoneNumber,
        String message,
        Instant scheduledTime) {
}