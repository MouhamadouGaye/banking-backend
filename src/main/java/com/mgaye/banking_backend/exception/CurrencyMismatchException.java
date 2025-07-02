package com.mgaye.banking_backend.exception;

// public class CurrencyMismatchException extends RuntimeException {
//     public CurrencyMismatchException(String accountCurrency, String requestCurrency) {
//         super(String.format("Account currency %s doesn't match request currency %s",
//                 accountCurrency, requestCurrency));
//     }
// }

public class CurrencyMismatchException extends RuntimeException {
    public CurrencyMismatchException(String message) {
        super(message);
    }
}