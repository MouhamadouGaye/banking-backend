package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.event.NotificationEvent;

public interface NotificationSender {
    void send(NotificationEvent event);
}