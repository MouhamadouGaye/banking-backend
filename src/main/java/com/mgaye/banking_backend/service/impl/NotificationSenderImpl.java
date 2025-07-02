package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.exception.NotificationException;
import com.mgaye.banking_backend.service.EmailService;
import com.mgaye.banking_backend.service.NotificationErrorHandler;
import com.mgaye.banking_backend.service.NotificationSender;
import com.mgaye.banking_backend.service.PushNotificationService;
import com.mgaye.banking_backend.service.SmsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationSenderImpl implements NotificationSender {
    private final EmailService emailService;
    private final PushNotificationService pushService;
    private final SmsService smsService;
    private final NotificationErrorHandler errorHandler;

    @Override
    public void send(NotificationEvent event) throws NotificationException {
        try {
            switch (event.getChannel()) {
                case EMAIL -> emailService.sendSimpleMessage(null, null, event.getMessage());
                case PUSH -> pushService.sendPushNotification(null, null, event.getMessage());
                case SMS -> smsService.send(event);
                default -> throw new NotificationException(event, "Unsupported channel: " + event.getChannel());
            }
        } catch (Exception e) {
            errorHandler.handleError(event, e);
            throw new NotificationException(event, "Failed to send " + event.getChannel() + " notification", e);
        }
    }
}