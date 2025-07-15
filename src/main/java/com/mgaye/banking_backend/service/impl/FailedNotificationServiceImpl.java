package com.mgaye.banking_backend.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.exception.NotificationException;
import com.mgaye.banking_backend.model.FailedNotification;
import com.mgaye.banking_backend.repository.FailedNotificationRepository;
import com.mgaye.banking_backend.service.FailedNotificationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FailedNotificationServiceImpl implements FailedNotificationService {
    private final FailedNotificationRepository repository;

    @Override
    @Transactional
    public void recordFailure(NotificationException exception) {
        NotificationEvent event = exception.getFailedEvent();

        FailedNotification failed = FailedNotification.builder()
                .userId(event.getUserId().toString())
                .eventType(event.getType().name())
                .channel(event.getChannel().name())
                .errorMessage(exception.getMessage())
                .timestamp(Instant.now())
                .build();

        repository.save(failed);
    }
}