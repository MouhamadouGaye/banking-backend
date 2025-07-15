package com.mgaye.banking_backend.exception;

import java.util.UUID;

public class AccountOwnershipException extends RuntimeException {
    public AccountOwnershipException(UUID accountId, UUID userId) {
        super(String.format("Account %s not owned by user %s", accountId, userId));
    }
}