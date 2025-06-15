package com.mgaye.banking_backend.dto;

import java.time.Instant;
import java.util.List;

import com.mgaye.banking_backend.dto.response.TicketResponseDto;

// SupportTicketDto.java
public record SupportTicketDto(
                String id,
                String subject,
                String message,
                String status,
                Instant createdAt,
                Instant updatedAt,
                List<TicketResponseDto> responses) {
}
