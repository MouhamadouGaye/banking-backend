package com.mgaye.banking_backend.exception;

/**
 * Custom exception for encryption/decryption failures
 */
public class EncryptionException extends Exception {
    public EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
