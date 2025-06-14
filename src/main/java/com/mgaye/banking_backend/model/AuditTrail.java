package com.mgaye.banking_backend.model;

import java.util.Map;

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
}