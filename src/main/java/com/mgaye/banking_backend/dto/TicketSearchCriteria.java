
package com.mgaye.banking_backend.dto;

import java.time.LocalDate;

public record TicketSearchCriteria(
        String status,
        String category,
        String priority,
        LocalDate createdAfter,
        LocalDate createdBefore,
        String assignedTo,
        String userId,
        int page,
        int size) {
}