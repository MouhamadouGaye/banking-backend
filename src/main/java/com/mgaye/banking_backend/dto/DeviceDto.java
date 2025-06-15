package com.mgaye.banking_backend.dto;

import com.mgaye.banking_backend.model.Device;

// dto/DeviceDto.java
public record DeviceDto(
        String id,
        String name,
        String ipAddress,
        String location,
        String lastUsed,
        String deviceType,
        boolean trusted,
        boolean currentDevice) {
    public static DeviceDto fromEntity(Device device, String currentDeviceId) {
        return new DeviceDto(
                device.getId(),
                device.getName(),
                device.getIpAddress(),
                device.getLocation(),
                device.getLastUsed().toString(),
                device.getDeviceType(),
                device.isTrusted(),
                device.getId().equals(currentDeviceId));
    }
}