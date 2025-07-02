package com.mgaye.banking_backend.model;

import java.math.BigDecimal;
import java.time.Instant;

import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import lombok.*;
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

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private BankAccount linkedAccount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal principalAmount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal outstandingBalance;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer termMonths;

    @Column(nullable = false)
    private Integer remainingTermMonths;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanStatus status;

    @Column(nullable = false)
    private Instant startDate;

    @Column
    private Instant endDate;

    @Column(nullable = false, length = 3)
    private String currency;

    @CreationTimestamp
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private LoanType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private LoanTerms terms;

    private BigDecimal requestedAmount;

    private Instant applicationDate;
    private BigDecimal amount;

    @Column(precision = 19, scale = 4)
    private BigDecimal totalInterest;

    @Column(precision = 19, scale = 2)
    private BigDecimal monthlyPayment;

    public enum LoanStatus {
        PENDING, ACTIVE, PAID_OFF, DEFAULTED, REFINANCED, CANCELLED, COLLECTION

    }

    public enum LoanType {
        PERSONAL, MORTGAGE, AUTO, BUSINESS, LINE_OF_CREDIT
    }

    public BigDecimal getMonthlyPayment() {
        return this.monthlyPayment;
    }

    // Add this method to your Loan class
    public BigDecimal getTotalInterest() {
        // Example calculation: total interest = principalAmount * interestRate *
        // termMonths / 12
        // Adjust this logic as per your actual interest calculation
        if (principalAmount == null || interestRate == null || termMonths == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal months = new BigDecimal(termMonths);
        BigDecimal yearlyInterest = principalAmount.multiply(interestRate);
        BigDecimal totalInterest = yearlyInterest.multiply(months).divide(new BigDecimal(12), BigDecimal.ROUND_HALF_UP);
        return totalInterest;
    }

    /**
     * Calculates the monthly payment using the loan's principal, interest rate, and
     * term.
     * Formula: M = P * r * (1 + r)^n / ((1 + r)^n - 1)
     */
    // public BigDecimal getMonthlyPayment() {
    // if (principalAmount == null || interestRate == null || termMonths == null ||
    // termMonths == 0) {
    // return BigDecimal.ZERO;
    // }
    // // Convert annual interest rate (e.g., 0.05 for 5%) to monthly rate
    // BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(12), 10,
    // BigDecimal.ROUND_HALF_UP);
    // BigDecimal onePlusRatePowN =
    // (BigDecimal.ONE.add(monthlyRate)).pow(termMonths);
    // BigDecimal numerator =
    // principalAmount.multiply(monthlyRate).multiply(onePlusRatePowN);
    // BigDecimal denominator = onePlusRatePowN.subtract(BigDecimal.ONE);
    // if (denominator.compareTo(BigDecimal.ZERO) == 0) {
    // return BigDecimal.ZERO;
    // }
    // return numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
    // }

    @Data
    @Builder
    public static class LoanTerms {
        private BigDecimal earlyRepaymentFee;
        private BigDecimal latePaymentFee;
        private Integer gracePeriodDays;
        private boolean allowsCoSigner;
        private boolean requiresCollateral;
    }
}