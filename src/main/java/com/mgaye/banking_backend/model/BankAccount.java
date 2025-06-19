package com.mgaye.banking_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.exception.BankingException;
import com.mgaye.banking_backend.exception.InsufficientFundsException;

@Entity
@Table(name = "bank_accounts", uniqueConstraints = {
        @UniqueConstraint(name = "uk_account_number", columnNames = "account_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "account_number", nullable = false, updatable = false, length = 34)
    private String accountNumber; // IBAN format

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 20)
    private AccountType accountType;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status;

    @Column(name = "overdraft_limit", precision = 19, scale = 4)
    private BigDecimal overdraftLimit;

    @Column(name = "minimum_balance", precision = 19, scale = 4)
    private BigDecimal minimumBalance;

    @Column(name = "interest_rate", precision = 5, scale = 4)
    private BigDecimal interestRate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // @Column(nullable = false)
    // private boolean active = true;

    @Column(name = "is_active")
    private boolean active;

    @Version
    private Long version;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private AccountFeatures features;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("timestamp DESC")
    private List<Transaction> transactions = new ArrayList<>();

    // Enum Definitions
    public enum AccountType {
        CHECKING("Checking Account"),
        SAVINGS("Savings Account"),
        BUSINESS("Business Account"),
        LOAN("Loan Account"),
        FIXED_DEPOSIT("Fixed Deposit");

        private final String description;

        AccountType(String description) {
            this.description = description;
        }
    }

    public enum AccountStatus {
        ACTIVE("Active"),
        FROZEN("Frozen"),
        CLOSED("Closed"),
        DORMANT("Dormant"),
        UNDER_REVIEW("Under Review");

        private final String description;

        AccountStatus(String description) {
            this.description = description;
        }
    }

    public boolean isActive() {
        return this.active;
    }

    // Nested JSON structure
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountFeatures {
        private boolean allowsOverdraft;
        private boolean allowsInternationalTransactions;
        private boolean hasDebitCard;
        private boolean hasCheckbook;
        private List<String> allowedTransactionTypes;
        private BigDecimal dailyWithdrawalLimit;
        private BigDecimal monthlyTransactionLimit;
    }

    public String getMaskedNumber() {
        if (this.accountNumber == null || this.accountNumber.length() < 4) {
            return "****";
        }
        String lastFour = this.accountNumber.substring(this.accountNumber.length() - 4);
        return "****" + lastFour;
    }

    // Business Logic Methods
    public boolean canWithdraw(BigDecimal amount) {
        BigDecimal availableBalance = balance.add(overdraftLimit != null ? overdraftLimit : BigDecimal.ZERO);
        return availableBalance.compareTo(amount) >= 0;
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        this.updatedAt = Instant.now();
    }

    public void withdraw(BigDecimal amount) {
        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException(id, balance, amount);
        }
        this.balance = this.balance.subtract(amount);
        this.updatedAt = Instant.now();
    }

    public void applyInterest() {
        if (interestRate != null && interestRate.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal interest = balance.multiply(interestRate)
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_EVEN);
            deposit(interest);
        }
    }

    public void debit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException(
                    "INVALID_AMOUNT",
                    "Debit amount must be positive",
                    HttpStatus.BAD_REQUEST);
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(this.id, this.balance, amount);
        }
        this.balance = this.balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException(
                    "INVALID_AMOUNT",
                    "Credit amount must be positive",
                    HttpStatus.BAD_REQUEST);
        }
        this.balance = this.balance.add(amount);
    }

}