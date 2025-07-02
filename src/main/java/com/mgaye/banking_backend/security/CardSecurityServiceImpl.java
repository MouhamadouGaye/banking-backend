// security/CardSecurityService.java
package com.mgaye.banking_backend.security;

import java.nio.file.attribute.UserPrincipal;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.repository.CardRepository;
import com.mgaye.banking_backend.service.CardSecurityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardSecurityServiceImpl implements CardSecurityService {
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;
    private final CardRepository cardRepository;
    private final Random random = new SecureRandom();

    @Override
    public String generateCVV() {
        return String.format("%03d", random.nextInt(1000)); // 3-digit CVV
    }

    @Override
    public String generatePIN() {
        return String.format("%04d", random.nextInt(10000)); // 4-digit PIN
    }

    @Override
    public String encrypt(String data) {
        return passwordEncoder.encode(data); // For PINs
    }

    @Override
    public String encryptCvv(String cvv) {
        return encryptionService.encryptCvv(cvv); // Stronger encryption for CVV
    }

    @Override
    public boolean verifyPin(String encryptedPin, String rawPin) {
        return passwordEncoder.matches(rawPin, encryptedPin);
    }

    @Override
    public boolean isCardOwner(Authentication authentication, String cardId) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return cardRepository.existsByIdAndUserId(cardId, user.getId());
    }
}