// ApiError.java
package com.mgaye.banking_backend.dto.error;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

// public record ApiError(
//         String code,
//         Instant timestamp,
//         String message,
//         Map<String, Object> details) {
//     public ApiError(String code, String message) {
//         this(code, Instant.now(), message, null);
//     }
// }// The constructor ApiError(int, LocalDateTime, String, String) is
//  // undefinedJava(134217858)

// dto/ApiError.java
public record ApiError(
        String code,
        Instant timestamp,
        String message,
        String type,
        Map<String, Object> details) {
    public ApiError(String code, String message, String type) {
        this(code, Instant.now(), message, type, null);
    }

    public ApiError(String code, String message, String type, Map<String, Object> details) {
        this(code, Instant.now(), message, type, details);
    }
}
