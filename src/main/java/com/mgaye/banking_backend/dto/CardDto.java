// CardDto.java

package com.mgaye.banking_backend.dto;

import java.time.Instant;
import java.time.LocalDate;

public record CardDto(
        String id,
        String cardNumberMasked,
        String cardType,
        String status,
        LocalDate expirationDate,
        String provider,
        Instant createdAt) {
}