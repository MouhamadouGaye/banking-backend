
package com.mgaye.banking_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.mgaye.banking_backend.model.BankAccount.AccountFeatures;

public record AccountDto(
                UUID id,
                UUID userId,
                String accountNumber,
                String accountType,
                BigDecimal balance,
                String currency,
                String status,
                AccountFeatures features, // or String if you'd prefer JSON-serialized
                Instant createdAt,
                Instant updatedAt) {
}
