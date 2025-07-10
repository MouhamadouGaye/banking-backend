package com.mgaye.banking_backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;
import java.util.Arrays;

// model/CreditScore.java
@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreditScore {
    private int score;
    private CreditRating rating;
    private LocalDate reportDate;
    private boolean fraudAlert;
    private final BigDecimal interestRate;

    public enum CreditRating {
        EXCELLENT(800, 850),
        GOOD(700, 799),
        FAIR(600, 699),
        POOR(300, 599);

        private final int minScore;
        private final int maxScore;

        CreditRating(int minScore, int maxScore) {
            this.minScore = minScore;
            this.maxScore = maxScore;

        }

        public static CreditRating fromScore(int score) {
            return Arrays.stream(values())
                    .filter(r -> score >= r.minScore && score <= r.maxScore)
                    .findFirst()
                    .orElse(POOR);
        }
    }

    public boolean isEligibleForLoan(BigDecimal amount) {
        // Example logic: assume a minimum score of 600 is required for loan eligibility
        return this.score >= 600 && !this.fraudAlert;
    }

    public boolean isFraudAlert() {
        return fraudAlert;
    }

    // // Example implementation, adjust logic as needed
    // public boolean isEligibleForLoan(BigDecimal amount) {
    // // Assume there is a method or field getMaxEligibleAmount()
    // // Replace with your actual eligibility logic
    // return getMaxEligibleAmount().compareTo(amount) >= 0;
    // }

    // Dummy method for illustration; replace or remove if already present
    public BigDecimal getMaxEligibleAmount() {
        // Return a default value or your actual logic
        return new BigDecimal("10000");
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }
}