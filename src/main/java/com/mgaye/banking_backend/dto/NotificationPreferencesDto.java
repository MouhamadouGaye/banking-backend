package com.mgaye.banking_backend.dto;

import lombok.Builder;
import lombok.Data;

// NotificationPreferencesDto.java
// public record NotificationPreferencesDto(
//         boolean email,
//         boolean sms,
//         boolean push) {
// }

@Data
@Builder
public class NotificationPreferencesDto {
        private boolean transactionEmails;
        private boolean marketingEmails;
        private boolean securityAlerts;
        private boolean pushNotifications;
        private boolean smsNotifications;
}