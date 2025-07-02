package com.mgaye.banking_backend.dto.response;

import java.time.LocalDate;

import com.mgaye.banking_backend.dto.request.CardIssuanceRequest.CardDesign;
import com.mgaye.banking_backend.dto.request.CardIssuanceRequest.CardType;

// public class CardResponse {
// private String id;
// private String CardId;
// private String cardNumber; // Masked
// private String cardHolderName;
// private CardType cardType;
// private CardDesign design;
// // private String cardType;
// // private String design;
// private String status;
// private LocalDate expirationDate;
// private String provider;
// private String linkedAccountId;
// private Instant createdAt;
// private boolean virtual;

// private String currency;
// }

// This part is when we working with record
public record CardResponse(
        String id,
        String CardId,
        String username,
        String cardNumber, // masked
        String cardHolderName,
        CardType cardType,
        CardDesign design,
        String currency,
        String status,
        LocalDate expirationDate,
        String provider,
        String linkedAccountId,
        boolean virtualCard) {
}
