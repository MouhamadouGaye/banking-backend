package com.mgaye.banking_backend.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.model.FailedNotification;
import com.mgaye.banking_backend.repository.FailedNotificationRepository;
import com.mgaye.banking_backend.service.NotificationErrorHandler;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationErrorHandlerImpl implements NotificationErrorHandler {
    private final FailedNotificationRepository failedNotificationRepository;

    public NotificationErrorHandlerImpl(FailedNotificationRepository failedNotificationRepository) {
        this.failedNotificationRepository = failedNotificationRepository;
    }

    @Override
    @Transactional
    public void handleError(NotificationEvent event, Exception exception) {
        log.error("Notification failed: {}", event, exception);

        FailedNotification failedNotification = FailedNotification.builder()
                .eventType(event.getType().name())
                .channel(event.getChannel().name())
                .userId(event.getUserId())
                .errorMessage(exception.getMessage())
                .timestamp(Instant.now())
                .retryCount(0)
                .build();

        failedNotificationRepository.save(failedNotification);
    }
}
