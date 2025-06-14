package com.mgaye.banking_backend.dto;

import java.time.Instant;
import java.time.LocalDate;

// UserDto.java
public record UserDto(
        String id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate dob,
        AddressDto address,
        String kycStatus,
        Instant createdAt,
        Instant updatedAt,
        UserSettingsDto settings) {
}