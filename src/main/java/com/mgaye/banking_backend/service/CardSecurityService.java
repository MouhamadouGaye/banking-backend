package com.mgaye.banking_backend.service;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;

public interface CardSecurityService {

    // String generateCVV();

    // String generatePIN();

    // String encrypt(String data);

    // boolean verifyPin(String encryptedPin, String rawPin);
    String encryptCvv();

    String generateCVV();

    String generatePIN();

    String encrypt(String data);

    // String encryptCvv(String cvv);

    boolean verifyPin(String encryptedPin, String rawPin);

    // boolean isCardOwner(Authentication authentication, String cardId);
}