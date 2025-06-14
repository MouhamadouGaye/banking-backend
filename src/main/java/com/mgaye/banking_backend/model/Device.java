package com.mgaye.banking_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "security_settings_id", nullable = false)
    private SecuritySettings securitySettings;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private String deviceHash;

    @Column(nullable = false)
    private Instant lastUsed;

    private String location;

    @Column(nullable = false)
    private boolean trusted;

    @Column(name = "os_info")
    private String osInfo;

    @Column(name = "browser_info")
    private String browserInfo;
}