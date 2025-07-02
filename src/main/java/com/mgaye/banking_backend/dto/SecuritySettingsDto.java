package com.mgaye.banking_backend.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// // SecuritySettingsDto.java
// public record SecuritySettingsDto(
//                 boolean twoFactorEnabled,
//                 boolean loginAlerts,
//                 List<DeviceDto> devices) {
// }

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecuritySettingsDto {
    private boolean twoFactorEnabled;
    private boolean loginAlerts;
    private boolean passwordChangeRequired;
    private Instant lastPasswordChange;
}
