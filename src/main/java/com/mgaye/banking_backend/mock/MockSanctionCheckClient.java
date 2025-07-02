package com.mgaye.banking_backend.mock;

import com.mgaye.banking_backend.client.SanctionCheckClient;
import com.mgaye.banking_backend.dto.response.SanctionCheckResult;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Profile("!prod") // Only active when not in production
public class MockSanctionCheckClient implements SanctionCheckClient {

    // Mock sanctioned names (in real app, this would come from a database or API)
    private static final Set<String> SANCTIONED_LAST_NAMES = Set.of(
            "Smith",
            "Johnson",
            "Williams");

    private static final Set<String> HIGH_RISK_COUNTRIES = Set.of(
            "IR", // Iran
            "KP", // North Korea
            "SY", // Syria
            "CU", // Cuba
            "RU" // Russia
    );

    @Override
    public SanctionCheckResult check(String firstName, String lastName, String country) {
        boolean hit = false;
        String hitDetails = null;

        // Check against sanctioned last names
        if (SANCTIONED_LAST_NAMES.contains(lastName)) {
            hit = true;
            hitDetails = "Last name matches sanctioned individuals";
        }

        // Check high-risk countries
        if (HIGH_RISK_COUNTRIES.contains(country)) {
            hit = true;
            hitDetails = (hitDetails != null ? hitDetails + "; " : "") +
                    "Country is high-risk: " + country;
        }

        return new SanctionCheckResult(hit, hitDetails);
    }
}