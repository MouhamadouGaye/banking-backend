package com.mgaye.banking_backend.exception;

public class TransactionNotCancelableException extends RuntimeException {
    public TransactionNotCancelableException(String id) {
        super("Transaction cannot be canceled: " + id);
    }
}
