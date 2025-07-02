package com.mgaye.banking_backend.exception;

import java.time.Instant;

public class SmsException extends RuntimeException {
    private final String phoneNumber;

    public SmsException(String phoneNumber, String message) {
        super(message);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
