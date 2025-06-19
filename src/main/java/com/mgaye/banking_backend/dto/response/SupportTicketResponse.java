package com.mgaye.banking_backend.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SupportTicketResponse(
        UUID ticketId,
        String subject,
        String status,
        String category,
        String priority,
        Instant createdAt,
        Instant lastUpdated,
        List<TicketMessageResponse> messages,
        String assignedTo) {
    public record TicketMessageResponse(
            UUID messageId,
            String content,
            Instant createdAt,
            boolean fromCustomer,
            List<AttachmentResponse> attachments) {
        public record AttachmentResponse(String url, String name) {
        }
    }
}