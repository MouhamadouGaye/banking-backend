package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.exception.NotificationException;

public interface NotificationSender {
    void send(NotificationEvent event) throws NotificationException;
}