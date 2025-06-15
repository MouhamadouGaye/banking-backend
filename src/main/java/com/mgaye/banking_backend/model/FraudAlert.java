package com.mgaye.banking_backend.model;

import com.mgaye.banking_backend.model.enums.FraudSeverity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// model/FraudAlert.java
@Entity
public class FraudAlert {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;
    @ManyToOne
    private BankAccount account;
    private String reason;
    private FraudSeverity severity;
    private boolean resolved;
}