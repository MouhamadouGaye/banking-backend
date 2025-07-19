package com.mgaye.banking_backend.exception;

// Custom exception for push notification errors
class PushNotificationException extends RuntimeException {
    public PushNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
