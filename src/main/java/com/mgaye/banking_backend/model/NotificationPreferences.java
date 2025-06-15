package com.mgaye.banking_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreferences {
    @Column(name = "email_notifications", nullable = false)
    private boolean email;

    @Column(name = "sms_notifications", nullable = false)
    private boolean sms;

    @Column(name = "push_notifications", nullable = false)
    private boolean push;

    @Column(name = "security_alerts", nullable = false)
    private boolean securityAlerts;

    @Column(name = "marketing_opt_in")
    private boolean marketingOptIn;
}