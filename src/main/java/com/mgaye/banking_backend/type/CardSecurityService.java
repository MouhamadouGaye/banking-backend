package com.mgaye.banking_backend.type;

public interface CardSecurityService {
    String generateCVV();

    String generatePIN();

    String encrypt(String data);

    boolean verifyPin(String encryptedPin, String inputPin);

    boolean verifyCVV(String encryptedCVV, String inputCVV);
}