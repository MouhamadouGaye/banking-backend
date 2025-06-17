package com.mgaye.banking_backend.service;

public interface CardSecurityService {

    String generateCVV();

    String generatePIN();

    String encrypt(String data);

    boolean verifyPin(String encryptedPin, String rawPin);
}