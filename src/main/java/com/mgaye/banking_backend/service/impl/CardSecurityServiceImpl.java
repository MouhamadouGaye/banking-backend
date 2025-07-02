package com.mgaye.banking_backend.service.impl;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.service.CardSecurityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardSecurityServiceImpl implements CardSecurityService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String generateCVV() {
        Random random = new Random();
        return String.format("%03d", random.nextInt(1000));
    }

    @Override
    public String generatePIN() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    @Override
    public String encrypt(String data) {
        return passwordEncoder.encode(data);
    }

    @Override
    public boolean verifyPin(String encryptedPin, String rawPin) {
        return passwordEncoder.matches(rawPin, encryptedPin);
    }
}