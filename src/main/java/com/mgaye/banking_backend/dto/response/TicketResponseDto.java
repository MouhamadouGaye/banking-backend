package com.mgaye.banking_backend.dto.response;

import java.time.Instant;

// TicketResponseDto.java
public record TicketResponseDto(
        String responder,
        String message,
        Instant timestamp) {
}