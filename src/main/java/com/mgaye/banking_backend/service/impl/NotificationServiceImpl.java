// NotificationServiceImpl.java

package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;

import javax.management.Notification;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.mgaye.banking_backend.exception.NotificationException;

import com.mgaye.banking_backend.dto.NotificationPreferencesDto;
import com.mgaye.banking_backend.dto.mapper.NotificationPreferencesMapper;
import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.NotificationPreferences;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.UserSettings;
import com.mgaye.banking_backend.repository.NotificationPreferencesRepository;
import com.mgaye.banking_backend.repository.NotificationRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.repository.UserSettingsRepository;
import com.mgaye.banking_backend.service.FailedNotificationService;
import com.mgaye.banking_backend.service.NotificationDispatcher;
import com.mgaye.banking_backend.service.NotificationSender;
import com.mgaye.banking_backend.service.NotificationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationPreferencesRepository preferencesRepository;
    private final NotificationPreferencesMapper preferencesMapper;
    private final UserRepository userRepository;
    private final NotificationSender notificationSender;
    private final FailedNotificationService failedNotificationService;

    // @Override
    // @Transactional
    // public void sendNotification(NotificationEvent event) {
    // if (event == null || event.getUserId() == null) {
    // throw new IllegalArgumentException("Notification event must have a valid user
    // ID");
    // }

    // User user = userRepository.findById(event.getUserId())
    // .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " +
    // event.getUserId()));

    // UserSettings settings = user.getUserSettings(); // Using the getter method

    // if (shouldSendNotification(event, settings)) {
    // try {
    // notificationSender.send(event);
    // } catch (NotificationException e) {
    // log.error("Failed to send notification: {}", event, e);
    // }
    // }
    // }

    @Override
    @Transactional
    public void sendNotification(NotificationEvent event) {
        try {
            User user = userRepository.findById(event.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            UserSettings settings = user.getUserSettings();
            if (shouldSendNotification(event, settings)) {
                notificationSender.send(event);
            }
        } catch (NotificationException e) {
            failedNotificationService.recordFailure(e);
            throw e; // Re-throw if you want the caller to handle it
        } catch (Exception e) {
            NotificationException wrapped = new NotificationException(event, "Unexpected error", e);
            failedNotificationService.recordFailure(wrapped);
            throw wrapped;
        }
    }

    @Override
    public boolean shouldSendNotification(NotificationEvent event, UserSettings settings) {

        if (event == null || settings == null) {
            return false;
        }

        return switch (event.getType()) {
            case TRANSACTION -> settings.isTransactionNotificationsEnabled();
            case SECURITY_ALERT -> settings.isSecurityAlertsEnabled();
            case MARKETING -> settings.isMarketingNotificationsEnabled();
            case SYSTEM -> settings.isMarketingNotificationsEnabled();

            default -> true;
        };
    }

    @Override
    @Transactional
    public NotificationPreferencesDto getPreferences(String userId) {
        return preferencesRepository.findByUserId(userId)
                .map(preferencesMapper::toDto)
                .orElseGet(preferencesMapper::toDefaultDto);
    }

    @Override
    @Transactional
    public void updateNotificationPreferences(String userId, NotificationPreferencesDto preferencesDto) {
        NotificationPreferences preferences = preferencesRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferences(userId));

        preferencesMapper.updateFromDto(preferencesDto, preferences);
        preferencesRepository.save(preferences);
    }

    @Override
    @Transactional
    public void updateSinglePreference(String userId, String preferenceType, boolean enabled) {
        switch (preferenceType.toLowerCase()) {
            case "transaction_emails" ->
                preferencesRepository.updateTransactionEmailsPreference(userId, enabled);
            case "marketing_emails" ->
                preferencesRepository.updateMarketingEmailsPreference(userId, enabled);
            case "security_alerts" ->
                preferencesRepository.updateSecurityAlertsPreference(userId, enabled);
            case "push_notifications" ->
                preferencesRepository.updatePushNotificationsPreference(userId, enabled);
            case "sms_notifications" ->
                preferencesRepository.updateSmsNotificationsPreference(userId, enabled);
            default -> throw new IllegalArgumentException("Invalid preference type: " + preferenceType);
        }
    }

    private NotificationPreferences createDefaultPreferences(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return NotificationPreferences.builder()
                .user(user)
                .transactionEmails(true)
                .securityAlerts(true)
                .pushNotifications(true)
                .marketingEmails(false)
                .smsNotifications(false)
                .build();
    }

}