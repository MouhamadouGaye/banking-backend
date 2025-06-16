package com.mgaye.banking_backend.exception;

// InvalidPasswordException.java
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}