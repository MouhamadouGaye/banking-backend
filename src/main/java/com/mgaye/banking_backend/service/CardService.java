package com.mgaye.banking_backend.service;

import java.util.List;
import java.util.UUID;

import com.mgaye.banking_backend.dto.request.CardIssuanceRequest;
import com.mgaye.banking_backend.dto.response.CardResponse;
import com.mgaye.banking_backend.model.enums.CardStatus;

public interface CardService {
    // Card Issuance
    CardResponse issueCard(CardIssuanceRequest request);

    // Card Status Management
    CardResponse updateStatus(String cardId, CardStatus status);

    // Card Information
    CardResponse getCardDetails(String cardId);

    List<CardResponse> getUserCards(UUID userId);

    // PIN Management
    void updatePin(String cardId, String currentPin, String newPin);

    // Security Utilities
    String maskCardNumber(String number);

    boolean verifyCardOwnership(UUID userId, String cardId);
}
