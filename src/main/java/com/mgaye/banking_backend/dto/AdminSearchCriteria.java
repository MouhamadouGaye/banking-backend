package com.mgaye.banking_backend.dto;

import java.time.LocalDate;
import java.util.List;

public record AdminSearchCriteria(
        String email,
        String name,
        Boolean active,
        LocalDate createdAfter,
        LocalDate createdBefore,
        List<String> roles,
        int page,
        int size) {
}