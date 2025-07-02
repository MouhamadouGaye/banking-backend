package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.exception.NotificationException;

public interface FailedNotificationService {
    void recordFailure(NotificationException exception);

}
