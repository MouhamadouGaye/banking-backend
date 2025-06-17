package com.mgaye.banking_backend.dto.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.mgaye.banking_backend.model.BankAccount.AccountFeatures;
import com.mgaye.banking_backend.model.BankAccount.AccountStatus;
import com.mgaye.banking_backend.model.BankAccount.AccountType;
import com.mgaye.banking_backend.model.User;

import jakarta.validation.constraints.NotBlank;

// AccountCreateRequest.java
public record AccountCreateRequest(
                @NotBlank String userId,
                @NotBlank String accountType, // CHECKING/SAVINGS/BUSINESS
                @NotBlank String currency,
                @NotBlank User user,
                @NotBlank BigDecimal balance,
                @NotBlank AccountStatus status) {
}
