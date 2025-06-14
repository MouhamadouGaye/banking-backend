package com.mgaye.banking_backend.dto;

import java.util.List;

// SecuritySettingsDto.java
public record SecuritySettingsDto(
        boolean twoFactorEnabled,
        boolean loginAlerts,
        List<DeviceDto> devices) {
}
