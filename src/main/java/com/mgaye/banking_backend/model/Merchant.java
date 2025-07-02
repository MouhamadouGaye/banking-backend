package com.mgaye.banking_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "merchants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    private String location;

    @Column(unique = true)
    private String merchantId; // Payment processor ID

    @Enumerated(EnumType.STRING)
    private MerchantStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum MerchantStatus {
        ACTIVE, SUSPENDED, BLACKLISTED
    }
}