package com.mgaye.banking_backend.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionHistoryRequest(
        @NotBlank String accountId,
        @NotNull LocalDate fromDate,
        @NotNull LocalDate toDate,
        List<String> transactionTypes,
        boolean includePending,
        @NotBlank String format) {
}
