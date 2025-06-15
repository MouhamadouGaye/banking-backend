package com.mgaye.banking_backend.model;

import jakarta.persistence.*;

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

    // ... [rest of your code] ...
}