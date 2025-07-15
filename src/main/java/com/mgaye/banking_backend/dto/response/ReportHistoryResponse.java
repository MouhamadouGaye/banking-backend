package com.mgaye.banking_backend.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

// // public record ReportHistoryResponse(
// //                 UUID requestId,
// //                 String reportType,
// //                 String status,
// //                 Instant requestedAt,
// //                 String format) {
// // }

// public record ReportHistoryResponse(
//         UUID requestId,
//         String reportType,
//         String status,
//         Instant requestedAt,
//         Instant completedAt,
//         String accountId,
//         String period,
//         LocalDate startDate,
//         LocalDate endDate) {
// }

public record ReportHistoryResponse(
        UUID requestId,
        String reportType,
        String status,
        Instant requestedAt,
        Instant completedAt,
        UUID accountId,
        String period,
        LocalDate startDate,
        LocalDate endDate) {

    // Helper method for converting from status response
    public static ReportHistoryResponse fromStatusResponse(
            ReportStatusResponse status,
            UUID accountId,
            String period,
            LocalDate startDate,
            LocalDate endDate) {
        return new ReportHistoryResponse(
                status.requestId(),
                status.reportType(),
                status.status(),
                status.requestedAt(),
                status.completedAt(),
                accountId,
                period,
                startDate,
                endDate);
    }
}