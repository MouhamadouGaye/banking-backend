package com.mgaye.banking_backend.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.mgaye.banking_backend.model.Transaction.TransactionType;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// In package: com.yourbank.dto.notifications
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAlert {
    private String transactionId;
    private String accountNumber;
    private BigDecimal amount;
    private String currency;
    private TransactionType type; // DEBIT/CREDIT
    private String merchant;
    private Instant timestamp;

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize TransactionAlert to JSON", e);
        }
    }
}