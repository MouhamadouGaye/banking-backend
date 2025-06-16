package com.mgaye.banking_backend.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String id) {
        super("Transaction not found: " + id);
    }
}