package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TicketCreateRequest {

    @NotNull
    private Long userId;

    @NotBlank
    @Size(max = 100)
    private String subject;

    @NotBlank
    @Size(max = 2000)
    private String description;

    @NotBlank
    private String category;

    @NotBlank
    private String priority; // LOW, MEDIUM, HIGH

    private String relatedAccountNumber;
}