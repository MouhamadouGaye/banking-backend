package com.mgaye.banking_backend.model;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
<<<<<<< HEAD
import org.hibernate.annotations.TypeDef;
=======
>>>>>>> master

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonType;

<<<<<<< HEAD
@Entity
// @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@TypeDef(name = "json", typeClass = JsonType.class)

=======
// // @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
// @TypeDef(name = "json", typeClass = JsonType.class)

@Entity
>>>>>>> master
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private Instant timestamp;

    private String description;

    @Column(name = "reference_id")
    private String referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionDirection direction;

    @Version
    private Long version;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private TransactionFee fee;

    // No need for extra dependency in newer versions:
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, FEE, REFUND, CHARGEBACK
    }

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, CANCELLED, REVERSED
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> master
