package com.mgaye.banking_backend.exception;

/**
 * Thrown when a transfer fails business validation rules
 */
public class TransferValidationException extends RuntimeException {

    public TransferValidationException(String message) {
        super(message);
    }

    public TransferValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    // Optional: Add error code support
    private String errorCode;

    public TransferValidationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}