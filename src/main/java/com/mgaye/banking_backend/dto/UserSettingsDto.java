// package com.mgaye.banking_backend.dto;

// // UserSettingsDto.java
// public record UserSettingsDto(
//         String language,
//         String theme,
//         NotificationPreferencesDto notificationPreferences,
//         String currency) {
// }

package com.mgaye.banking_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// UserSettingsDto.java

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDto {
        private String language;
        private String theme;
        private String timezone;
        private boolean emailNotifications;
        private boolean pushNotifications;
}