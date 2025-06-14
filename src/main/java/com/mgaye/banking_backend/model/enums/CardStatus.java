package com.mgaye.banking_backend.model.enums;

public enum CardStatus {
    ACTIVE("Active", true),
    INACTIVE("Inactive", false),
    BLOCKED("Blocked", false),
    EXPIRED("Expired", false),
    REPORTED_LOST("Reported Lost", false),
    REPORTED_STOLEN("Reported Stolen", false),
    PENDING_ACTIVATION("Pending Activation", false);

    private final String description;
    private final boolean canTransact;

    CardStatus(String description, boolean canTransact) {
        this.description = description;
        this.canTransact = canTransact;
    }

    public boolean canProcessTransaction() {
        return canTransact;
    }

    public static boolean isAllowedForTransaction(CardStatus status) {
        return status != null && status.canTransact;
    }
}