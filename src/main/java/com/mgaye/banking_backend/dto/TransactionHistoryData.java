package com.mgaye.banking_backend.dto;

public record TransactionHistoryData(
        String userId,
        String accountId,
        LocalDate startDate,
        LocalDate endDate,
        List<TransactionItem> transactions) {
}