package com.mgaye.banking_backend.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

// // BankingException.java (Base)
// public abstract class BankingException extends RuntimeException {
//     private final String errorCode;
//     private final HttpStatus status;
//     private final Map<String, Object> details;

//     public BankingException(String errorCode, String message, HttpStatus status) {
//         this(errorCode, message, status, null);
//     }

//     public BankingException(String errorCode, String message,
//             HttpStatus status, Map<String, Object> details) {
//         super(message);
//         this.errorCode = errorCode;
//         this.status = status;
//         this.details = details;
//     }
// }

public class BankingException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Map<String, Object> details;

    public BankingException(String errorCode, String message, HttpStatus httpStatus) {
        this(errorCode, message, httpStatus, Map.of());
    }

    public BankingException(String errorCode, String message,
            HttpStatus httpStatus, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = details;
    }

    // Getters
    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}