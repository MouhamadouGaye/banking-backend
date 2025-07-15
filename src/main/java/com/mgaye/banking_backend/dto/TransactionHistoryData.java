package com.mgaye.banking_backend.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TransactionHistoryData(
        UUID userId,
        UUID accountId,
        LocalDate startDate,
        LocalDate endDate,
        List<TransactionItem> transactions,
        String type) {
}

// (String, String, LocalDate, LocalDate, List<TransactionItem>, String) is
// undefinedJava(134217858)
