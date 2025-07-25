package com.mgaye.banking_backend.service.impl;

import com.mgaye.banking_backend.service.CardService;
import com.mgaye.banking_backend.type.CardNumberGenerator;

import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
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

@Service
@Transactional
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository accountRepository;
    private final com.mgaye.banking_backend.service.CardSecurityService cardSecurityService;
    private final CardNumberGenerator cardNumberGenerator;
    private final CardMapper cardMapper;

    @Override
    public CardResponse updateStatus(String cardId, CardStatus status) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        card.setStatus(Card.CardStatus.valueOf(status.name()));
        return cardMapper.toCardResponse(cardRepository.save(card));
    }

    @Override
    public CardResponse issueCard(CardIssuanceRequest request) {
        // Validate user and account
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BankAccount account = accountRepository.findByIdAndUserId(request.accountId(), request.userId());
        if (account == null) {
            throw new AuthorizationServiceException("Account not found or inaccessible");
        }

        // Validate currency match
        if (!account.getCurrency().equals(request.currency())) {
            throw new IllegalArgumentException("Account currency doesn't match requested card currency");
        }

        // Generate card details
        String fullCardNumber = cardNumberGenerator.generate(request.cardType(),
                request.design());
        String cvv = cardSecurityService.generateCVV();
        String pin = request.virtualCard() ? null : cardSecurityService.generatePIN();

        // Create and save card
        Card card = Card.builder()
                .user(user)
                .cardNumber(cardSecurityService.encrypt(fullCardNumber))
                .cardNumberMasked(maskCardNumber(fullCardNumber))
                .cardType(Card.CardType.valueOf(request.cardType().toString()))
                .design(Card.CardDesign.valueOf(request.design().toString()))
                .currency(request.currency())
                .status(request.virtualCard() ? Card.CardStatus.ACTIVE : Card.CardStatus.PENDING)
                .expirationDate(request.expiryDate())
                .cvvEncrypted(cardSecurityService.encrypt(cvv))
                .pinEncrypted(pin != null ? cardSecurityService.encrypt(pin) : null)
                .linkedAccount(account)
                .provider(determineCardProvider(fullCardNumber))
                .virtual(request.virtualCard())
                .cardNumber(cardSecurityService.encrypt(fullCardNumber)) // Encrypt PAN
                .cvvEncrypted(cardSecurityService.encrypt(cvv))
                .pinEncrypted(pin != null ? cardSecurityService.encrypt(pin) : null)
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
    public List<CardResponse> getUserCards(UUID userId) {
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
        return number.replaceAll("^(\\d{4})(\\d{4})(\\d{4})(\\d{4})$",
                "****-****-****-$4");
    }

    @Override
    public boolean verifyCardOwnership(UUID userId, String cardId) {
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
