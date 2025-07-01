package com.mgaye.banking_backend.dto;

import java.time.LocalDate;
import java.util.List;

public record TransactionHistoryData(
                String userId,
                String accountId,
                LocalDate startDate,
                LocalDate endDate,
                List<TransactionItem> transactions,
                String type) {
}

// (String, String, LocalDate, LocalDate, List<TransactionItem>, String) is
// undefinedJava(134217858)
