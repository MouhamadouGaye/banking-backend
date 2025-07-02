package com.mgaye.banking_backend.exception;

public class LoanRejectedException extends RuntimeException {
    public LoanRejectedException(String message) {
        super(message);
    }
}