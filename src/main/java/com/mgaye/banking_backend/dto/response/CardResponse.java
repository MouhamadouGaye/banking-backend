package com.mgaye.banking_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

import com.mgaye.banking_backend.model.enums.CardStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {
    private String id;
    private String cardNumber; // Masked
    private String cardHolderName;
    private String cardType;
    private String status;
    private LocalDate expirationDate;
    private String provider;
    private String linkedAccountId;
    private Instant createdAt;
    private boolean virtual;
    private String design;
    private String currency;
}