// package com.mgaye.banking_backend.exception;

// import java.util.Map;

// import org.springframework.http.HttpStatus;

// public class BankingException extends RuntimeException {
//     private final String errorCode;
//     private final HttpStatus httpStatus;
//     private final Map<String, Object> details;

//     public BankingException(String errorCode, String message, HttpStatus httpStatus) {
//         this(errorCode, message, httpStatus, Map.of());
//     }

//     // public BankingException(String errorCode, String message,
//     // HttpStatus httpStatus, Map<String, Object> details) {
//     // super(message);
//     // this.errorCode = errorCode;
//     // this.httpStatus = httpStatus;
//     // this.details = Map.of();

//     // }

//     public BankingException(String errorCode, String message, HttpStatus httpStatus, Throwable cause) {
//         super(message, cause);
//         this.errorCode = errorCode;
//         this.httpStatus = httpStatus;
//         this.details = Map.of(); // Or extract details from `cause` if needed
//     }

//     // Getters
//     public String getErrorCode() {
//         return errorCode;
//     }

//     public HttpStatus getHttpStatus() {
//         return httpStatus;
//     }

//     public Map<String, Object> getDetails() {
//         return details;
//     }
// }

package com.mgaye.banking_backend.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class BankingException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Map<String, Object> details;

    // Primary constructor (with cause and details)
    public BankingException(String errorCode, String message, HttpStatus httpStatus, Map<String, Object> details,
            Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = details != null ? details : Map.of();
    }

    // Constructor with cause only (empty details)
    public BankingException(String errorCode, String message, HttpStatus httpStatus, Throwable cause) {
        this(errorCode, message, httpStatus, Map.of(), cause);
    }

    // Constructor with details only (no cause)
    public BankingException(String errorCode, String message, HttpStatus httpStatus, Map<String, Object> details) {
        this(errorCode, message, httpStatus, details, null);
    }

    // Minimal constructor (no cause, no details)
    public BankingException(String errorCode, String message, HttpStatus httpStatus) {
        this(errorCode, message, httpStatus, Map.of(), null);
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
