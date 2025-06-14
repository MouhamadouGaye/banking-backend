package com.mgaye.banking_backend.model;

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
}