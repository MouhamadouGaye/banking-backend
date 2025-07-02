package com.mgaye.banking_backend.client.prod;

import com.mgaye.banking_backend.client.SanctionCheckClient;
import com.mgaye.banking_backend.dto.response.SanctionCheckResult;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("prod")
public class WorldCheckSanctionClient implements SanctionCheckClient {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public WorldCheckSanctionClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.apiUrl = "https://api.worldcheck.com/v2/screen";
    }

    @Override
    public SanctionCheckResult check(String firstName, String lastName, String country) {
        // In a real implementation, this would call the actual sanction screening
        // service
        ScreenRequest request = new ScreenRequest(firstName, lastName, country);
        ScreenResponse response = restTemplate.postForObject(apiUrl, request, ScreenResponse.class);

        return new SanctionCheckResult(
                response.isHit(),
                response.getHitDetails());
    }

    // Request/Response DTOs would go here
    private static class ScreenRequest {
        // fields, constructor, getters

        private String firstName;
        private String lastName;
        private String country;

        public ScreenRequest(String firstName, String lastName, String Country) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.country = country;
        }
    }

    private static class ScreenResponse {
        // fields, constructor, getters

        private boolean hit;
        private String hitDetails;

        public boolean isHit() {
            return hit;
        }

        public String getHitDetails() {
            return hitDetails;
        }

    }
}