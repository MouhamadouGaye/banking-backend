package com.mgaye.banking_backend.util;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

// util/AccountNumberGenerator.java
@Component
public class AccountNumberGenerator {
    private static final String BANK_CODE = "123"; // Your bank code
    private static final AtomicLong sequence = new AtomicLong(1);

    public String generate() {
        long nextSeq = sequence.getAndIncrement();
        String base = BANK_CODE + String.format("%09d", nextSeq);
        return base + calculateCheckDigit(base);
    }

    private static int calculateCheckDigit(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            sum += (i % 2 == 0) ? digit * 2 : digit;
        }
        return (10 - (sum % 10)) % 10;
    }
}