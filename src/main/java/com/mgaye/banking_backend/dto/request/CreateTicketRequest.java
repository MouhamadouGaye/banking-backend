package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// CreateTicketRequest.java
public record CreateTicketRequest(
        @NotBlank String userId,
        @NotBlank @Size(min = 5, max = 100) String subject,
        @NotBlank @Size(min = 10, max = 2000) String message) {
}