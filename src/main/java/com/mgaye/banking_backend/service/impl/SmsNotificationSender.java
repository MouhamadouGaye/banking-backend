package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.exception.NotificationException;
import com.mgaye.banking_backend.service.NotificationSender;
import com.mgaye.banking_backend.service.SmsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmsNotificationSender implements NotificationSender {
    private final SmsService smsService;

    @Override
    public void send(NotificationEvent event) throws NotificationException {
        // Implementation using smsService
    }
}