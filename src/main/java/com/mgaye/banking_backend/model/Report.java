package com.mgaye.banking_backend.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String reportType; // STATEMENT, TRANSACTION_HISTORY, TAX, etc.

    @Column(nullable = false)
    private Instant requestedAt;

    @Column
    private Instant completedAt;

    @Column(nullable = false, length = 20)
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED

    @Column(length = 255)
    private String filePath;

    @Column(length = 50)
    private String format; // PDF, CSV, EXCEL

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private ReportParameters parameters;

    @Data
    @Builder
    public static class ReportParameters {
        private String accountId;
        private String period;
        private LocalDate fromDate;
        private LocalDate toDate;
        private List<String> transactionTypes;
        private boolean includePending;
        private String timezone;
    }
}