package com.mgaye.banking_backend.model;

import java.time.Instant;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.mgaye.banking_backend.dto.request.CardIssuanceRequest.CardDesign;
import com.mgaye.banking_backend.model.enums.CardStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

// // model/Card.java
// @Entity
// @Table(name = "cards")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class Card {
//     @Id
//     @GeneratedValue(strategy = GenerationType.UUID)
//     private String id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "user_id", nullable = false)
//     private User user;

//     @Column(name = "card_number", nullable = false)
//     private String cardNumber; // Encrypted

//     @Column(name = "card_number_masked", nullable = false)
//     private String cardNumberMasked;

//     // ------------
//     @Enumerated(EnumType.STRING)
//     @Column(nullable = false)
//     private CardDesign design;

//     @Column(nullable = false, length = 3)
//     private String currency;

//     @Column(nullable = false)
//     private boolean virtual;

//     // ------------

//     @Enumerated(EnumType.STRING)
//     @Column(nullable = false)
//     private CardType cardType;

//     @Enumerated(EnumType.STRING)
//     @Column(nullable = false)
//     private CardStatus status;

//     @Column(name = "expiration_date", nullable = false)
//     private LocalDate expirationDate;

//     @Column(name = "cvv_encrypted", nullable = false)
//     private String cvvEncrypted;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "linked_account_id", nullable = false)
//     private BankAccount linkedAccount;

//     @Enumerated(EnumType.STRING)
//     @Column(nullable = false)
//     private CardProvider provider;

//     @CreationTimestamp
//     private Instant createdAt;

//     public enum CardType {
//         DEBIT, CREDIT, PREPAID
//     }

//     public enum CardProvider {
//         VISA, MASTERCARD, AMEX, DISCOVER
//     }

// }

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "card_number", nullable = false)
    private String cardNumber; // Encrypted PAN

    @Column(name = "card_number_masked", nullable = false)
    private String cardNumberMasked;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardDesign design;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private boolean virtual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "cvv_encrypted", nullable = false)
    private String cvvEncrypted; // Encrypted CVV

    @Column(name = "pin_encrypted")
    private String pinEncrypted; // Encrypted PIN (nullable for virtual cards)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_account_id", nullable = false)
    private BankAccount linkedAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardProvider provider;

    @CreationTimestamp
    private Instant createdAt;

    // Manual getters/setters if not using Lombok
    public String getPinEncrypted() {
        return this.pinEncrypted;
    }

    public void setPinEncrypted(String pinEncrypted) {
        this.pinEncrypted = pinEncrypted;
    }

    public enum CardType {
        DEBIT, CREDIT, PREPAID
    }

    public enum CardStatus {
        PENDING, ACTIVE, BLOCKED, EXPIRED
    }

    public enum CardProvider {
        VISA, MASTERCARD, AMEX, DISCOVER
    }

    public enum CardDesign {
        STANDARD, GOLD, PLATINUM, CUSTOM
    }
}