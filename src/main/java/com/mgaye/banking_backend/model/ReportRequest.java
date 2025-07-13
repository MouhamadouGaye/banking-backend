package com.mgaye.banking_backend.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String reportType;

    @Column(nullable = false)
    private String period;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    private String format;

    private String timezone;

    @CreationTimestamp
    private Instant requestedAt;

    @Column(nullable = false)
    private String status;

    private Instant completedAt;
    private String storageKey;
    private String downloadUrl;
    private String errorMessage;

    public ReportRequest(UUID id, String userId, String accountId, String reportType, String period,
            LocalDate startDate, LocalDate endDate, String format, String timezone, Instant requestedAt, String status,
            Instant completedAt, String storageKey, String downloadUrl) {
        this.id = id;
        this.userId = userId;
        this.accountId = accountId;
        this.reportType = reportType;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
        this.format = format;
        this.timezone = timezone;
        this.requestedAt = requestedAt;
        this.status = status;
        this.completedAt = completedAt;
        this.storageKey = storageKey;
        this.downloadUrl = downloadUrl;
    }
}