package com.mgaye.banking_backend.model;

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
}