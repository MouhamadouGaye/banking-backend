package com.mgaye.banking_backend.model;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "security_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecuritySettings {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "two_factor_enabled", nullable = false)
    private boolean twoFactorEnabled;

    @Column(name = "login_alerts", nullable = false)
    private boolean loginAlerts;

    @OneToMany(mappedBy = "securitySettings", cascade = CascadeType.ALL)
    private List<Device> devices;

    @Column(name = "last_password_change")
    private Instant lastPasswordChange;

    @Column(name = "require_password_change")
    private boolean requirePasswordChange;
}