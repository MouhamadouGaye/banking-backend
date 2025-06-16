package com.mgaye.banking_backend.util;

import com.mgaye.banking_backend.dto.request.CardIssuanceRequest;

public interface CardNumberGenerator {
    String generate(CardIssuanceRequest.CardType cardType, CardIssuanceRequest.CardDesign design);
}