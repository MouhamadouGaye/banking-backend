package com.mgaye.banking_backend.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.mgaye.banking_backend.model.Notification;

// NotificationDispatcher.java
@Service
@RequiredArgsConstructor
public class NotificationDispatcher {
    private final EmailService emailService;
    private final SmsService smsService;
    private final PushNotificationService pushService;
    private final NotificationRepository notificationRepo;

    @Async
    @TransactionalEventListener
    public void handleNotificationEvent(NotificationEvent event) {
        Notification notification = createNotificationRecord(event);

        if (event.user().getSettings().getNotificationPreferences().isEmail()) {
            emailService.send(event);
        }

        if (event.user().getSettings().getNotificationPreferences().isSms()) {
            smsService.send(event);
        }

        notificationRepo.save(notification.markDelivered());
    }

    private Notification createNotificationRecord(NotificationEvent event) {
        return Notification.builder()
                .userId(event.user().getId())
                .type(event.type())
                .message(event.message())
                .metadata(event.metadata())
                .build();
    }
}