package com.mgaye.banking_backend.dto.response;

import lombok.Data;

import java.time.LocalDate;

import com.mgaye.banking_backend.model.enums.CardStatus;

@Data
public class CardResponse {
    private Long id;
    private String cardNumber;
    private String cardHolderName;
    private String cardType;
    private LocalDate expiryDate;
    private String cvv;
    private CardStatus status;
    private Double dailyLimit;
    private Double availableBalance;
    private String linkedAccountNumber;
}