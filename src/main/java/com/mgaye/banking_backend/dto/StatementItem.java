// dto/StatementItem.java
package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.springframework.transaction.TransactionStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.Transaction.TransactionType;

import jakarta.validation.constraints.Positive;

public record StatementItem(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Instant date,
        String description,
        String reference,
        @Positive BigDecimal amount,
        BigDecimal balance,
        TransactionType type,
        TransactionStatus status) {
    public String getFormattedAmount() {
        String sign = "";
        if (type == TransactionType.CREDIT) {

            sign = "+";
        } else if (type == TransactionType.DEBIT) {
            sign = "-";
        }
        return String.format("%s%.2f", sign, amount);
    }
}