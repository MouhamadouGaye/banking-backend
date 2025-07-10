// package com.mgaye.banking_backend.service;

// import java.util.regex.Pattern;

// import org.springframework.context.annotation.Profile;
// import org.springframework.stereotype.Service;

// @Service
// @Profile({ "dev", "test" })
// public class MockBankCodeService implements BankCodeService {

// // Simple regex patterns for basic validation
// private static final Pattern SWIFT_PATTERN =
// Pattern.compile("[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?");
// private static final Pattern ACCOUNT_PATTERN =
// Pattern.compile("[A-Z0-9]{8,34}");

// @Override
// public boolean isValidInternationalCode(String code) {
// if (code == null)
// return false;
// return SWIFT_PATTERN.matcher(code).matches();
// }

// @Override
// public boolean validateAccountDetails(String accountNumber, String
// routingNumber) {
// return accountNumber != null &&
// ACCOUNT_PATTERN.matcher(accountNumber).matches() &&
// (routingNumber == null || isValidInternationalCode(routingNumber));
// }
// }