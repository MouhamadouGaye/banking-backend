package com.mgaye.banking_backend.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ReportStatusResponse(
        UUID requestId,
        String reportType,
        String status,
        Instant requestedAt,
        Instant completedAt,
        String downloadUrl) {
}