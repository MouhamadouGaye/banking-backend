package com.mgaye.banking_backend.service.impl;

import org.springframework.stereotype.Service;
import lombok.*;
import com.mgaye.banking_backend.repository.CardRepository;

// CardServiceImpl.java
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepo;
    private final CardSecurityService securityService;

    @Override
    public Card issueCard(CardIssuanceRequest request) {
        String maskedNumber = securityService.generateCardNumber();
        String encryptedCvv = securityService.encryptCvv(generateRandomCvv());

        Card card = Card.builder()
                .cardNumberMasked(maskCardNumber(maskedNumber))
                .cardType(request.cardType())
                .expirationDate(LocalDate.now().plusYears(3))
                .cvvMasked("***")
                .status(CardStatus.ACTIVE)
                .build();

        securityService.storeSecureCardDetails(card.getId(), maskedNumber, encryptedCvv);
        return cardRepo.save(card);
    }

    private String maskCardNumber(String number) {
        return "****-****-****-" + number.substring(12);
    }
}

// // service/CardService.java
// @Service
// @RequiredArgsConstructor
// public class CardServiceImpl implements CardService {
// private final CardRepository cardRepository;
// private final CardSecurityService cardSecurityService;
// private final AccountRepository accountRepository;

// @Override
// @Transactional
// public Card issueCard(CardIssuanceRequest request) {
// BankAccount account = accountRepository.findById(request.accountId())
// .orElseThrow(() -> new AccountNotFoundException(request.accountId()));

// Card card = new Card();
// card.setCardType(request.cardType());
// card.setProvider(request.provider());
// card.setStatus(CardStatus.PENDING_ACTIVATION);
// card.setExpirationDate(LocalDate.now().plusYears(3));
// card.setCardNumber(cardSecurityService.generateCardNumber());
// card.setCvvEncrypted(cardSecurityService.encryptCvv(generateRandomCvv()));
// card.setLinkedAccount(account);

// return cardRepository.save(card);
// }

// private String generateRandomCvv() {
// return RandomStringUtils.randomNumeric(3);
// }
// }