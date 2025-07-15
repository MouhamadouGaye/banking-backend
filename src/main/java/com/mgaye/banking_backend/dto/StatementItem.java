// dto/StatementItem.java
package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mgaye.banking_backend.model.StatementItemEntity;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.Transaction.TransactionDirection;
import com.mgaye.banking_backend.model.Transaction.TransactionStatus;
import com.mgaye.banking_backend.model.Transaction.TransactionType;

import jakarta.validation.constraints.Positive;

// public record StatementItem(
//         @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Instant date,
//         String description,
//         String reference,
//         @Positive BigDecimal amount,
//         BigDecimal balance,
//         TransactionType type,
//         TransactionStatus status) {
//     public String getFormattedAmount() {
//         String sign = "";
//         if (type == TransactionType.CREDIT) {

//             sign = "+";
//         } else if (type == TransactionType.DEBIT) {
//             sign = "-";
//         }
//         return String.format("%s%.2f", sign, amount);
//     }
// }

// public record StatementItem(
//         @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Instant timestamp,
//         String description,
//         String referenceNumber,
//         @Positive BigDecimal amount,
//         BigDecimal balance,
//         TransactionType type,
//         TransactionStatus status) {

//     public String getFormattedDate() {
//         return timestamp.atZone(ZoneId.systemDefault())
//                 .format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
//     }

//     public String getFormattedAmount() {
//         String sign = type == TransactionType.CREDIT ? "+" : "-";
//         return String.format("%s%s %.2f", sign, currency(), amount.abs());
//     }

//     public String getFormattedBalance() {
//         return String.format("%s %.2f", currency(), balance);
//     }

//     private String currency() {
//         // Assuming currency comes from transaction or account
//         return "USD"; // Replace with actual currency logic
//     }
// }

// public record StatementItem(
//         String id,
//         @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Instant date,
//         String reference,
//         @Positive BigDecimal amount,
//         String description,
//         TransactionType type,
//         TransactionStatus status,
//         TransactionDirection direction) {

//     public StatementItem(StatementItemEntity entity) {
//         this(
//                 entity.getId().toString(),
//                 entity.getDate(),
//                 entity.getReference(),
//                 entity.getAmount(),
//                 entity.getDescription(),
//                 entity.getType(),
//                 entity.getStatus(),
//                 entity.getDirection());
//     }

//     public String getFormattedAmount() {
//         String sign = direction == TransactionDirection.INBOUND ? "+" : "-";
//         return String.format("%s%s %.2f", sign, type.toString().charAt(0), amount.abs());
//     }

//     public String getFormattedDate() {
//         return date.atZone(ZoneId.systemDefault())
//                 .format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a"));
//     }
// }

public record StatementItem(
        String id,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Instant date,
        String reference,
        @Positive BigDecimal amount,
        String description,
        TransactionType type,
        TransactionStatus status,
        TransactionDirection direction) {

    // Explicit canonical constructor (required for custom constructors)
    public StatementItem(
            String id,
            Instant date,
            String reference,
            BigDecimal amount,
            String description,
            TransactionType type,
            TransactionStatus status,
            TransactionDirection direction) {
        this.id = id;
        this.date = date;
        this.reference = reference;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.status = status;
        this.direction = direction;
    }

    // Entity conversion constructor
    public StatementItem(StatementItemEntity entity) {
        this(
                entity.getId().toString(),
                entity.getDate(),
                entity.getReference(),
                entity.getAmount(),
                entity.getDescription(),
                entity.getType(),
                entity.getStatus(),
                entity.getDirection());
    }

    // // Static factory method for conversion
    // public static StatementItem fromEntity(StatementItemEntity entity) {
    // return new StatementItem(
    // entity.getId().toString(),
    // entity.getDate(),
    // entity.getReference(),
    // entity.getAmount(),
    // entity.getDescription(),
    // entity.getType(),
    // entity.getStatus(),
    // entity.getDirection());
    // }

    // ... (getFormattedAmount and getFormattedDate methods remain unchanged)

    public String getFormattedAmount() {
        String sign = direction == TransactionDirection.INBOUND ? "+" : "-";
        return String.format("%s%s %.2f", sign, type.toString().charAt(0), amount.abs());
    }

    public String getFormattedDate() {
        return date.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a"));
    }
}