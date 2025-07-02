package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.event.NotificationEvent;

public interface NotificationErrorHandler {
    void handleError(NotificationEvent event, Exception exception);
}