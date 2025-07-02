package com.mgaye.banking_backend.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Email Notifications
    @Column(name = "email_notifications_enabled", nullable = false)
    private boolean emailNotificationsEnabled = true;

    @Column(nullable = false)
    private boolean transactionEmails = true;

    @Column(nullable = false)
    private boolean marketingEmails = false;

    @Column(nullable = false)
    private boolean securityAlerts = true;

    @Column(nullable = false)
    private boolean pushNotifications = true;

    @Column(nullable = false)
    private boolean smsNotifications = false;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    // Helper methods
    public boolean isEmailNotificationsEnabled() {
        return emailNotificationsEnabled;
    }

    public boolean isTransactionEmailsEnabled() {
        return transactionEmails;
    }

    public boolean isSecurityAlertsEnabled() {
        return securityAlerts;
    }

    public boolean isMarketingEmailsEnabled() {
        return marketingEmails;
    }

    public boolean isPushNotificationsEnabled() {
        return pushNotifications;
    }

    public boolean isSmsNotificationsEnabled() {
        return smsNotifications;
    }
}
