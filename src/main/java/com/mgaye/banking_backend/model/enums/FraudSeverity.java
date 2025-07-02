// model/enums/FraudSeverity.java
package com.mgaye.banking_backend.model.enums;

public enum FraudSeverity {
    LOW("Low Risk", 1),
    MEDIUM("Medium Risk", 2),
    HIGH("High Risk", 3),
    CRITICAL("Critical Risk", 4);

    private final String description;
    private final int level;

    FraudSeverity(String description, int level) {
        this.description = description;
        this.level = level;
    }

    public static FraudSeverity fromRiskScore(int score) {
        if (score >= 80)
            return CRITICAL;
        if (score >= 60)
            return HIGH;
        if (score >= 40)
            return MEDIUM;
        return LOW;
    }
}