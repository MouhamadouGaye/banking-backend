package com.mgaye.banking_backend.model;

import java.time.Instant;
import java.util.Map;

<<<<<<< HEAD
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
=======
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.vladmihalcea.hibernate.type.json.JsonType;
>>>>>>> master

import lombok.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String principal; // Username or system

<<<<<<< HEAD
    @Type(JsonType.class)
=======
    // @Type(JsonTyp.class)
    // @Column(columnDefinition = "jsonb")
    // private Map<String, Object> parameters;

    @JdbcTypeCode(SqlTypes.JSON)
>>>>>>> master
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> parameters;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private String status; // SUCCESS/FAILED

    private String errorDetails;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;
}