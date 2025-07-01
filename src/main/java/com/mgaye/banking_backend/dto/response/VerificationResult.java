package com.mgaye.banking_backend.dto.response;

import java.util.List;

public record VerificationResult(boolean success, List<String> documentReasons, String sanctionDetails) {
    public static VerificationResult successResult() {
        return new VerificationResult(true, List.of(), null);
    }

    public static VerificationResult failed(List<String> docReasons, String sanctionDetails) {
        return new VerificationResult(false, docReasons, sanctionDetails);
    }
}