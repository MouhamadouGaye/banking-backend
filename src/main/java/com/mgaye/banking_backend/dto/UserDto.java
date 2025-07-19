package com.mgaye.banking_backend.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

// UserDto.java
public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate dob,
        AddressDto address,
        String kycStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserSettingsDto settings) {
}