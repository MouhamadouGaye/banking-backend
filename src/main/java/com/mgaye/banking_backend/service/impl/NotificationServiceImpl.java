// NotificationServiceImpl.java

package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;
@Service
import javax.management.Notification;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.NotificationPreferencesDto;
import com.mgaye.banking_backend.dto.mapper.NotificationPreferencesMapper;
import com.mgaye.banking_backend.event.NotificationEvent;
import com.mgaye.banking_backend.model.NotificationPreferences;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.UserSettings;
import com.mgaye.banking_backend.repository.NotificationPreferencesRepository;
import com.mgaye.banking_backend.repository.NotificationRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.repository.UserSettingsRepository;
import com.mgaye.banking_backend.service.NotificationDispatcher;
import com.mgaye.banking_backend.service.NotificationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationPreferencesRepository preferencesRepository;
    private final NotificationPreferencesMapper preferencesMapper;
    private final UserRepository userRepository;

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

    @Override
    @Transactional
    public void updateSinglePreference(String userId, String preferenceType, boolean enabled) {
        switch (preferenceType.toLowerCase()) {
            case "transaction_emails" -> 
                preferencesRepository.updateTransactionEmailsPreference(userId, enabled);
            case "marketing_emails" -> 
                preferencesRepository.updateMarketingEmailsPreference(userId, enabled);
            // Add other cases as needed
            default -> throw new IllegalArgumentException("Invalid preference type: " + preferenceType);
        }
    }
}