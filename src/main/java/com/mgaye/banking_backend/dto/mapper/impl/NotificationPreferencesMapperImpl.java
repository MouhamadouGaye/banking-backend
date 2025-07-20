package com.mgaye.banking_backend.dto.mapper.impl;

import org.springframework.stereotype.Component;

import com.mgaye.banking_backend.dto.NotificationPreferencesDto;
import com.mgaye.banking_backend.dto.mapper.NotificationPreferencesMapper;
import com.mgaye.banking_backend.model.NotificationPreferences;

@Component
public class NotificationPreferencesMapperImpl implements NotificationPreferencesMapper {

    @Override
    public NotificationPreferencesDto toDto(NotificationPreferences preferences) {
        if (preferences == null)
            return toDefaultDto();

        return NotificationPreferencesDto.builder()
                .transactionEmails(preferences.isTransactionEmails())
                .marketingEmails(preferences.isMarketingEmails())
                .securityAlerts(preferences.isSecurityAlerts())
                .pushNotifications(preferences.isPushNotifications())
                .smsNotifications(preferences.isSmsNotifications())
                .build();
    }

    @Override
    public void updateFromDto(NotificationPreferencesDto dto, NotificationPreferences entity) {
        if (dto == null || entity == null)
            return;

        entity.setTransactionEmails(dto.isTransactionEmails());
        entity.setMarketingEmails(dto.isMarketingEmails());
        entity.setSecurityAlerts(dto.isSecurityAlerts());
        entity.setPushNotifications(dto.isPushNotifications());
        entity.setSmsNotifications(dto.isSmsNotifications());
    }

    @Override
    public NotificationPreferencesDto toDefaultDto() {
        return NotificationPreferencesDto.builder()
                .transactionEmails(true)
                .marketingEmails(false)
                .securityAlerts(true)
                .pushNotifications(true)
                .smsNotifications(false)
                .build();
    }
}