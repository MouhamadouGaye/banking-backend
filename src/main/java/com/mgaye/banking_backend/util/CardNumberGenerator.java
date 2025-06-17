package com.mgaye.banking_backend.util;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.request.CardIssuanceRequest;
import com.mgaye.banking_backend.model.Card;

// public interface CardNumberGenerator {
//     String generate(CardIssuanceRequest.CardType cardType, CardIssuanceRequest.CardDesign design);
// }

@Service
public class CardNumberGenerator {
    private static final Random random = new Random();

    public String generate(Card.CardType cardType, String design) {
        String prefix = getPrefixForCardType(cardType);
        String middle = generateRandomDigits(12 - prefix.length());
        String number = prefix + middle;
        return number + calculateLuhnCheckDigit(number);
    }

    private String getPrefixForCardType(Card.CardType cardType) {
        return switch (cardType) {
            case DEBIT -> "4"; // Visa
            case CREDIT -> "5"; // Mastercard
            case PREPAID -> "6"; // Discover
        };
    }

    private String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return (sum * 9) % 10;
    }
}