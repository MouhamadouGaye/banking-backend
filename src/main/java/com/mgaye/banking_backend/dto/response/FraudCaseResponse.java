package com.mgaye.banking_backend.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record FraudCaseResponse(
        UUID caseId,
        UUID reportedByAdminId,
        UUID assignedToAdminId,
        String severity,
        String status,
        String caseType,
        String description,
        Instant reportedAt,
        Instant lastUpdatedAt,
        Instant resolvedAt,
        String resolutionNotes,
        List<String> transactionIds,
        List<String> accountNumbers,
        FraudDetails details,
        Map<String, Object> metadata) {

    public record FraudDetails(
            String evidenceType,
            List<String> evidenceUrls,
            Map<String, Object> customData) {
    }
}