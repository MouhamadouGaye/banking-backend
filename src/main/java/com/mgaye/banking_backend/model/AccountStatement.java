package com.mgaye.banking_backend.model;

<<<<<<< HEAD
import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

// model/AccountStatement.java
@Entity
public class AccountStatement {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;
    private String accountId;
    private Instant generatedAt;
    private Instant periodStart;
    private Instant periodEnd;
    @Lob
    private byte[] pdfContent;
=======
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "account_statements")
public class AccountStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(nullable = false)
    private Instant generatedAt;

    @Column(nullable = false)
    private Instant periodStart;

    @Column(nullable = false)
    private Instant periodEnd;

    @Lob
    @Column(nullable = false)
    private byte[] pdfContent;

    @Column(length = 50)
    private String statementNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatementStatus status = StatementStatus.GENERATED;

    // Constructors
    public AccountStatement() {
    }

    public AccountStatement(String accountId, Instant periodStart, Instant periodEnd, byte[] pdfContent) {
        this.accountId = accountId;
        this.generatedAt = Instant.now();
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.pdfContent = pdfContent;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public Instant getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Instant periodStart) {
        this.periodStart = periodStart;
    }

    public Instant getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Instant periodEnd) {
        this.periodEnd = periodEnd;
    }

    public byte[] getPdfContent() {
        return pdfContent;
    }

    public void setPdfContent(byte[] pdfContent) {
        this.pdfContent = pdfContent;
    }

    public String getStatementNumber() {
        return statementNumber;
    }

    public void setStatementNumber(String statementNumber) {
        this.statementNumber = statementNumber;
    }

    public StatementStatus getStatus() {
        return status;
    }

    public void setStatus(StatementStatus status) {
        this.status = status;
    }

    // Enum for statement status
    public enum StatementStatus {
        GENERATED,
        DELIVERED,
        VIEWED,
        ARCHIVED
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AccountStatement that = (AccountStatement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString
    @Override
    public String toString() {
        return "AccountStatement{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", generatedAt=" + generatedAt +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", statementNumber='" + statementNumber + '\'' +
                ", status=" + status +
                '}';
    }
>>>>>>> master
}