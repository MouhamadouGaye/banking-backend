package com.mgaye.banking_backend.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record StatementRequest(
        @NotBlank String accountId,
        @NotBlank String period, // MONTHLY, QUARTERLY, YEARLY, CUSTOM
        LocalDate customStart,
        LocalDate customEnd,
        @NotBlank String format,
        String timezone) {
}
