package com.mgaye.banking_backend.dto.request;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;

public record FraudReviewRequest(
        @NotBlank String severity,
        @NotBlank String description,
        List<String> transactionIds,
        List<String> accountNumbers,
        FraudDetails details) {
    public record FraudDetails(
            String evidenceType,
            Map<String, Object> customData) {
    }
}
