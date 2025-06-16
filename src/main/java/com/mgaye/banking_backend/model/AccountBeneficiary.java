package com.mgaye.banking_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// model/AccountBeneficiary.java

@Entity
@Table(name = "account_beneficiaries")
public class AccountBeneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Corrected this line
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private BankAccount account;

    @Column(nullable = false)
    private String beneficiaryName;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String routingNumber;

    // Constructors, getters, and setters
    public AccountBeneficiary() {
    }

}