package com.mgaye.banking_backend.client.prod;

import com.mgaye.banking_backend.client.DocumentVerificationClient;
import com.mgaye.banking_backend.dto.response.DocumentVerificationResult;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
@Profile("prod")
public class AcmeDocumentVerificationClient implements DocumentVerificationClient {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public AcmeDocumentVerificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.apiUrl = "https://api.acmekyc.com/v1/verify";
    }

    @Override
    public DocumentVerificationResult verify(String firstName, String lastName, LocalDate dob) {
        // In a real implementation, this would call the actual KYC service
        VerifyRequest request = new VerifyRequest(firstName, lastName, dob);
        VerifyResponse response = restTemplate.postForObject(apiUrl, request, VerifyResponse.class);

        return new DocumentVerificationResult(
                response.isVerified(),
                response.getReasons());
    }

    // Request/Response DTOs would go here

    private static class VerifyRequest {
        private String firstName;
        private String lastName;
        private String dob;

        public VerifyRequest(String firstName, String lastName, LocalDate dob) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob != null ? dob.toString() : null;
        }

    }

    @Data
    private static class VerifyResponse {
        private boolean verified;
        private List<String> reasons;

        public boolean isVerified() {
            return verified;
        }

        public List<String> getReasons() {
            return reasons;
        }

        // You may add constructor(s) if needed
    }
}