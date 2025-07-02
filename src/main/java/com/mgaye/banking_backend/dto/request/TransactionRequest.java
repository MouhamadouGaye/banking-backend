package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;

import com.mgaye.banking_backend.model.TransactionDirection;
import com.mgaye.banking_backend.model.Transaction.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionRequest(
                @NotBlank String accountId,
                @Positive @NotNull BigDecimal amount,
                @NotBlank String currency,
                @NotNull TransactionType type,
                @NotNull TransactionDirection direction,
                String description,
                String referenceId,
                String merchantId,
                String destinationAccountId // For transfers only
) {
        public boolean isTransfer() {
                return type == TransactionType.TRANSFER && destinationAccountId != null;
        }

        public boolean isDebit() {
                return direction == TransactionDirection.INBOUND;
        }

        // Helper method for creating modified copies
        public TransactionRequest withDescription(String newDescription) {
                return new TransactionRequest(
                                accountId,
                                amount,
                                currency,
                                type,
                                direction,
                                newDescription,
                                referenceId,
                                merchantId,
                                destinationAccountId);
        }
}