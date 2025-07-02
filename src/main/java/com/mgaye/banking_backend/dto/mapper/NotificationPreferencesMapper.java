package com.mgaye.banking_backend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mgaye.banking_backend.dto.NotificationPreferencesDto;
import com.mgaye.banking_backend.model.NotificationPreferences;

@Mapper(componentModel = "spring")
public interface NotificationPreferencesMapper {
    NotificationPreferencesDto toDto(NotificationPreferences preferences);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDto(NotificationPreferencesDto dto, @MappingTarget NotificationPreferences entity);

    default NotificationPreferencesDto toDefaultDto() {
        return NotificationPreferencesDto.builder()
                .transactionEmails(true)
                .marketingEmails(false)
                .securityAlerts(true)
                .pushNotifications(true)
                .smsNotifications(false)
                .build();
    }
}

// @Mapper(componentModel = "spring")
// public interface NotificationPreferencesMapper {
// NotificationPreferencesDto toDto(NotificationPreferences preferences);

// @Mapping(target = "user", ignore = true)
// @Mapping(target = "id", ignore = true)
// @Mapping(target = "createdAt", ignore = true)
// @Mapping(target = "updatedAt", ignore = true)
// void updateFromDto(NotificationPreferencesDto dto, @MappingTarget
// NotificationPreferences entity);

// default NotificationPreferencesDto toDefaultDto() {
// return NotificationPreferencesDto.builder()
// .transactionEmails(true)
// .marketingEmails(false)
// .securityAlerts(true)
// .pushNotifications(true)
// .smsNotifications(false)
// .build();
// }
// }