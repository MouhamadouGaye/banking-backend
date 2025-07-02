package com.mgaye.banking_backend.exception;

import java.time.Instant;

public record SmsDeliveryReceipt(
        String messageId,
        String status,
        Instant timestamp) {
}