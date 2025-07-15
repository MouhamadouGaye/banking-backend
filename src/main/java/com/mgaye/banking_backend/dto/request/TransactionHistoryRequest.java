package com.mgaye.banking_backend.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.model.Transaction.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// public record TransactionHistoryRequest(
//         @NotBlank String accountId,
//         @NotNull LocalDate fromDate,
//         @NotNull LocalDate toDate,
//         List<String> transactionTypes,
//         boolean includePending,
//         @NotBlank String format) {
// }

public record TransactionHistoryRequest(
        UUID accountId, // null for all accounts
        @NotBlank String period,
        LocalDate customStart,
        LocalDate customEnd,
        @NotBlank String format,
        String timezone,
        TransactionType transactionType) {
}
