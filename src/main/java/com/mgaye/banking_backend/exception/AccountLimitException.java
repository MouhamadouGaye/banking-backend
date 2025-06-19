package com.mgaye.banking_backend.exception;

public class AccountLimitException extends RuntimeException {
    public AccountLimitException(String message) {
        super(message);
    }
}