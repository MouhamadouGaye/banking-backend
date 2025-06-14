package com.mgaye.banking_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Table(name = "transaction_audits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    private AuditAction action;

    private String changedBy; // User ID or system process
    private String fieldChanged;
    private String oldValue;
    private String newValue;

    @CreationTimestamp
    private Instant createdAt;

    public enum AuditAction {
        CREATE, UPDATE, STATUS_CHANGE, DELETE, CORRECTION
    }
}