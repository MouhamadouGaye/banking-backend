package com.mgaye.banking_backend.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ReportHistoryResponse(
                UUID requestId,
                String reportType,
                String status,
                Instant requestedAt,
                String format) {
}

// public record ReportHistoryResponse(
// UUID requestId,
// String reportType,
// String status,
// Instant requestedAt,
// Instant completedAt,
// String accountId,
// String period,
// LocalDate startDate,
// LocalDate endDate) {}