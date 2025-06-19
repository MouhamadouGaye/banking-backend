package com.mgaye.banking_backend.exception;

public class InvalidAccountRequestException extends RuntimeException {
    public InvalidAccountRequestException(String message) {
        super(message);
    }
}
