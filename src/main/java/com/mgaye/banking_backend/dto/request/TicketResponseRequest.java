package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TicketResponseRequest {

    @NotNull
    private Long ticketId;

    @NotNull
    private Long responderId;

    @NotBlank
    @Size(max = 2000)
    private String response;

    private boolean closeTicket;

    private String internalNotes;
}