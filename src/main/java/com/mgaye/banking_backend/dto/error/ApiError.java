// ApiError.java
package com.mgaye.banking_backend.dto.error;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public record ApiError(
        String code,
        String message,
        Instant timestamp,
        Map<String, Object> details) {
    public ApiError(String code, String message) {
        this(code, message, Instant.now(), null);
    }
}