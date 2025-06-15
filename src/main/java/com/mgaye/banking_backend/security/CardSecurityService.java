// security/CardSecurityService.java
package com.mgaye.banking_backend.security;

import java.nio.file.attribute.UserPrincipal;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.repository.CardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardSecurityService {
    private final EncryptionService encryptionService;
    private final CardRepository cardRepository;

    public boolean isCardOwner(Authentication authentication, String cardId) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return cardRepository.existsByIdAndUserId(cardId, user.getId());
    }

    public String generateCardNumber() {
        String number = "4" + RandomStringUtils.randomNumeric(15); // Visa format
        return number.substring(0, 4) + "-****-****-" + number.substring(12);
    }

    public String encryptCvv(String cvv) {
        return encryptionService.encrypt(cvv);
    }
}