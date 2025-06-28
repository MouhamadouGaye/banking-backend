package com.mgaye.banking_backend.exception;

import com.mgaye.banking_backend.event.NotificationEvent;

public class NotificationException extends RuntimeException {
    private final NotificationEvent failedEvent;

    public NotificationException(NotificationEvent event, String message) {
        super("Failed to send notification: " + message);
        this.failedEvent = event;
    }

    public NotificationException(NotificationEvent event, String message, Throwable cause) {
        super("Failed to send notification: " + message, cause);
        this.failedEvent = event;
    }

    // Constructor with context (for cases where event isn't available)
    public NotificationException(String message, Throwable cause) {
        super(message, cause);
        this.failedEvent = null;
        this.context = null;
    }

    public NotificationEvent getFailedEvent() {
        return failedEvent;
    }
}