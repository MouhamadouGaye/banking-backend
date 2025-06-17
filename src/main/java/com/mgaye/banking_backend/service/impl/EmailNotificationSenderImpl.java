package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.EmailMessage;
import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.event.NotificationEvent.NotificationChannel;
import com.mgaye.banking_backend.exception.NotificationException;
import com.mgaye.banking_backend.service.EmailService;
import com.mgaye.banking_backend.service.JavaEmailService;
import com.mgaye.banking_backend.service.NotificationSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationSenderImpl implements NotificationSender {
    private final JavaEmailService emailService;

    @Override
    public void send(NotificationEvent event) throws NotificationException {
        if (event.getChannel() != NotificationChannel.EMAIL) {
            throw new NotificationException(event,
                    "Only email notifications are currently supported");
        }

        try {
            EmailMessage email = convertToEmail(event);
            emailService.send(email);
        } catch (Exception e) {
            throw new NotificationException(event, "Failed to send email notification", e);
        }
    }

    private EmailMessage convertToEmail(NotificationEvent event) {
        return EmailMessage.builder()
                .to(event.getUserId() + "@yourdomain.com") // Adjust as needed
                .subject(event.getTitle())
                .body(event.getMessage())
                .build();
    }
}
