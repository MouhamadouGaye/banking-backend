package com.mgaye.banking_backend.model;

import java.util.Map;

<<<<<<< HEAD
import org.hibernate.annotations.Type;

import jakarta.persistence.Entity;

// model/AuditTrail.java
@Entity
public class AuditTrail {
    private String changedBy;
    private String changeType; // CREATE/UPDATE/DELETE
    @Type(value = "jsonb")
    private Map<String, Object> oldValues;
    @Type(value = "jsonb")
    private Map<String, Object> newValues;
=======
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// // model/AuditTrail.java
// @Entity
// public class AuditTrail {
//     private String changedBy;
//     private String changeType; // CREATE/UPDATE/DELETE
//     @Type(value = "jsonb")
//     private Map<String, Object> oldValues;
//     @Type(value = "jsonb")
//     private Map<String, Object> newValues;
// }

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
>>>>>>> master
}