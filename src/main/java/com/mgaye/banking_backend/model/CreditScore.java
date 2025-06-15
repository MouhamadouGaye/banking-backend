package com.mgaye.banking_backend.model;

import java.time.LocalDate;

import lombok.*;
import java.util.Arrays;

// model/CreditScore.java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditScore {
    private int score;
    private CreditRating rating;
    private LocalDate reportDate;
    private boolean fraudAlert;

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
}