package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.dto.response.ValidationResponse;

public interface BankCodeService {
    /**
     * Validates international bank codes (SWIFT/BIC, IBAN, etc.)
     * 
     * @param code The bank code to validate
     * @return true if valid, false otherwise
     */
    boolean isValidInternationalCode(String code);

    /**
     * Validates account details against banking networks
     * 
     * @param accountNumber The account number
     * @param routingNumber The routing/swift code
     * @return true if account details are valid
     */
    boolean validateAccountDetails(String accountNumber, String routingNumber);

    /**
     * Get detailed validation results including account holder info
     */
    ValidationResponse getDetailedValidation(String accountNumber, String routingNumber);
}
