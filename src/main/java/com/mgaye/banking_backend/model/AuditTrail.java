package com.mgaye.banking_backend.model;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_trails")
public class AuditTrail {
    @Column(name = "changed_by")
    private String changedBy;
    @Column(name = "change_type")
    private String changeType; // CREATE/UPDATE/DELETE
    // ... other fields ...

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> oldValues;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> newValues;

    // ... rest of the entity ...
}