package com.mgaye.banking_backend.model;

import lombok.*;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 5)
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ThemePreference theme;

    @Embedded
    private NotificationPreferences notificationPreferences;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "transaction_notifications", nullable = false)
    private boolean transactionNotificationsEnabled = true;

    @Column(name = "security_alerts", nullable = false)
    private boolean securityAlertsEnabled = true;

    @Column(name = "marketing_notifications", nullable = false)
    private boolean marketingNotificationsEnabled = false;

    // Boolean getters for better readability
    public boolean isTransactionNotificationsEnabled() {
        return transactionNotificationsEnabled;
    }

    public boolean isSecurityAlertsEnabled() {
        return securityAlertsEnabled;
    }

    public boolean isMarketingNotificationsEnabled() {
        return marketingNotificationsEnabled;
    }

    public enum ThemePreference {
        LIGHT, DARK, SYSTEM
    }
}