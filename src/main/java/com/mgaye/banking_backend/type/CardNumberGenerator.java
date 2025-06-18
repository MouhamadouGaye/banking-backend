
package com.mgaye.banking_backend.type;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.request.CardIssuanceRequest;
import com.mgaye.banking_backend.dto.request.CardIssuanceRequest.CardDesign;
import com.mgaye.banking_backend.model.Card.CardType;

@Service
public class CardNumberGenerator {
    private static final Random random = new Random();

    public String generate(CardIssuanceRequest.CardType cardType, CardIssuanceRequest.CardDesign design) {
        String prefix = getPrefixForCardType(cardType, design);
        String middle = generateRandomDigits(15 - prefix.length()); // 16 digits total with check digit
        String numberWithoutCheck = prefix + middle;
        return numberWithoutCheck + calculateLuhnCheckDigit(numberWithoutCheck);
    }

    private String getPrefixForCardType(CardIssuanceRequest.CardType cardType, CardIssuanceRequest.CardDesign design) {
        // Base prefixes by card network
        String basePrefix = switch (cardType) {
            case DEBIT -> "4"; // Visa
            case CREDIT -> "5"; // Mastercard
            case PREPAID -> "6"; // Discover
        };

        // Design modifiers
        return switch (design) {
            case STANDARD -> basePrefix;
            case GOLD -> "5" + basePrefix; // Gold cards
            case PLATINUM -> "6" + basePrefix; // Platinum cards
            case CUSTOM -> "9" + basePrefix; // Custom cards
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

    // Helper method for testing
    public static boolean isValidCardNumber(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}