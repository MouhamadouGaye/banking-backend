package com.mgaye.banking_backend.dto.error;

// ValidationError.java
public record ValidationError(
        String field,
        String message,
        Object rejectedValue) {
}