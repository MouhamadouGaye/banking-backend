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

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonType;

// @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)

// // @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
// @TypeDef(name = "json", typeClass = JsonType.class)

@Entity
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

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private Instant date;

    private String description;

    @Column(name = "reference_id")
    private String referenceNumber;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id")
    private BankAccount destinationAccount;

    @OneToOne
    @JoinColumn(name = "linked_transaction_id")
    private Transaction linkedTransaction;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, FEE, REFUND, CHARGEBACK, DEBIT, CREDIT
    }

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, CANCELLED, REVERSED
    }

    public enum TransactionDirection {
        INBOUND, OUTBOUND
    }
}

// @RestController. this code is not taken into account
// public class Transsaction {

// private final TransactionService transactionService;

// // In your controllers:
// @PostMapping("/transactions")
// public ResponseEntity<TransactionDto> createTransaction(
// @Valid @RequestBody TransactionRequest request,
// @CurrentUser User user) {
// Transaction transaction = transactionService.create(request, user);
// return ResponseEntity.ok(mapToDto(transaction));
// }

// private TransactionDto mapToDto(Transaction transaction) {
// return new TransactionDto(
// transaction.getId(),
// transaction.getAccount().getId(),
// transaction.getType().name(),
// transaction.getAmount(),
// transaction.getCurrency(),
// transaction.getStatus().name(),
// transaction.getTimestamp(),
// transaction.getDescription(),
// transaction.getReferenceId(),
// mapMerchantToDto(transaction.getMerchant()),
// transaction.getDirection().name());
// }

// }