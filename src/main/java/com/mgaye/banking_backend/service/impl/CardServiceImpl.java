package com.mgaye.banking_backend.service.impl;

import com.mgaye.banking_backend.service.CardService;
import com.mgaye.banking_backend.type.CardNumberGenerator;

import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import lombok.*;

import com.mgaye.banking_backend.dto.mapper.CardMapper;
import com.mgaye.banking_backend.dto.request.CardIssuanceRequest;
import com.mgaye.banking_backend.dto.response.CardResponse;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.BankAccount;
import com.mgaye.banking_backend.model.Card;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.enums.CardStatus;
import com.mgaye.banking_backend.repository.AccountRepository;
import com.mgaye.banking_backend.repository.BankAccountRepository;
import com.mgaye.banking_backend.repository.CardRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.security.CardSecurityService;

@Service
@Transactional
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository accountRepository;
    private final CardSecurityService cardSecurityService;
    private final CardNumberGenerator cardNumberGenerator;
    private final CardMapper cardMapper;

    @Override
    public CardResponse updateStatus(String cardId, String status) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        card.setStatus(CardStatus.valueOf(status));
        return cardMapper.toCardResponse(cardRepository.save(card));
    }

    @Override
    @Transactional
    public CardResponse issueCard(CardIssuanceRequest request) {
        // Validate user and account
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BankAccount account = accountRepository.findById(request.accountId())
                .filter(a -> a.getUser().getId().equals(request.userId()))
                .orElseThrow(() -> new AuthorizationServiceException("Account not found or inaccessible"));

        // Generate card details
        String fullCardNumber = cardNumberGenerator.generate(request.cardType(), request.design());
        String cvv = cardSecurityService.generateCVV();
        String pin = request.virtualCard() ? null : cardSecurityService.generatePIN();

        // Create and save card
        Card card = Card.builder()
                .user(user)
                .cardNumber(cardSecurityService.encrypt(fullCardNumber))
                .cardNumberMasked(maskCardNumber(fullCardNumber))
                .cardType(request.cardType())
                .status(request.virtualCard() ? CardStatus.ACTIVE : CardStatus.PENDING)
                .expirationDate(request.expiryDate())
                .cvvEncrypted(cardSecurityService.encrypt(cvv))
                .linkedAccount(account)
                .provider(determineCardProvider(fullCardNumber))
                .build();

        Card savedCard = cardRepository.save(card);
        return cardMapper.toCardResponse(savedCard);
    }

    @Override
    @Transactional(readOnly = true)
    public CardResponse getCardDetails(String cardId) {
        return cardRepository.findById(cardId)
                .map(cardMapper::toCardResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardResponse> getUserCards(String userId) {
        return cardRepository.findByUserId(userId).stream()
                .map(cardMapper::toCardResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updatePin(String cardId, String currentPin, String newPin) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        if (!cardSecurityService.verifyPin(card.getPinEncrypted(), currentPin)) {
            throw new SecurityException("Current PIN is incorrect");
        }

        card.setPinEncrypted(cardSecurityService.encrypt(newPin));
        cardRepository.save(card);
    }

    @Override
    public String maskCardNumber(String number) {
        return number.replaceAll("^(\\d{4})(\\d{4})(\\d{4})(\\d{4})$", "****-****-****-$4");
    }

    @Override
    public boolean verifyCardOwnership(String userId, String cardId) {
        return cardRepository.existsByIdAndUserId(cardId, userId);
    }

    private Card.CardProvider determineCardProvider(String cardNumber) {
        if (cardNumber.startsWith("4"))
            return Card.CardProvider.VISA;
        if (cardNumber.startsWith("5"))
            return Card.CardProvider.MASTERCARD;
        if (cardNumber.startsWith("3"))
            return Card.CardProvider.AMEX;
        if (cardNumber.startsWith("6"))
            return Card.CardProvider.DISCOVER;
        throw new IllegalArgumentException("Invalid card number format");
    }
}