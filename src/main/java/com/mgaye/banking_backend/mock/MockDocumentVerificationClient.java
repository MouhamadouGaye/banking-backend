package com.mgaye.banking_backend.mock;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.client.DocumentVerificationClient;
import com.mgaye.banking_backend.dto.response.DocumentVerificationResult;

@Service
@Profile("!prod") // Only active when not in production
public class MockDocumentVerificationClient implements DocumentVerificationClient {

    @Override
    public DocumentVerificationResult verify(String firstName, String lastName, LocalDate dob) {
        // Mock verification logic
        List<String> reasons = new ArrayList<>();

        // Check if user is at least 18 years old
        if (Period.between(dob, LocalDate.now()).getYears() < 18) {
            reasons.add("User must be at least 18 years old");
        }

        // Check for empty names
        if (firstName == null || firstName.trim().isEmpty()) {
            reasons.add("First name cannot be empty");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            reasons.add("Last name cannot be empty");
        }

        boolean verified = reasons.isEmpty();
        return new DocumentVerificationResult(verified, reasons);
    }
}