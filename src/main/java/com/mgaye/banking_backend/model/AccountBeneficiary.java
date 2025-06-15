package com.mgaye.banking_backend.model;

<<<<<<< HEAD
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// model/AccountBeneficiary.java
@Entity
public class AccountBeneficiary {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;
    @ManyToOne
    private BankAccount account;
    private String beneficiaryName;
    private String accountNumber;
    private String routingNumber;
=======
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
>>>>>>> master
}