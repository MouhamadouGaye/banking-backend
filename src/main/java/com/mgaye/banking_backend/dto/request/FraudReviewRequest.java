package com.mgaye.banking_backend.dto.request;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FraudReviewRequest(
                @NotNull UUID userId, // Added missing userId field
                @NotBlank String severity,
                @NotBlank String description,
                @NotBlank String caseType,
                List<String> transactionIds,
                List<String> accountNumbers,
                List<String> evidenceUrls, // Added missing evidenceUrls
                FraudDetails details,
                Map<String, Object> metadata) { // Added missing metadata

        public record FraudDetails(
                        String evidenceType,
                        Map<String, Object> customData) {
        }
}