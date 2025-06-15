package com.mgaye.banking_backend.client;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.*;

// client/ExternalCreditServiceClient.java
public interface ExternalCreditServiceClient {
    @PostMapping("/credit-score")
    CreditScoreResponse getCreditScore(@RequestBody CreditScoreRequest request);

    @Data
    class CreditScoreRequest {
        private String firstName;
        private String lastName;
        private LocalDate dob;
        private String address;
    }

    @Data
    class CreditScoreResponse {
        private int score;
        private String rating;
        private LocalDate reportDate;
    }
}