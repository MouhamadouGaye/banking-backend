package com.mgaye.banking_backend.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// Option 1: As standalone enum (recommended for simple cases)
public enum TransactionDirection {
    INBOUND,
    OUTBOUND;

    // Optional converter if needed
    @Converter(autoApply = true)
    public static class DirectionConverter implements AttributeConverter<TransactionDirection, String> {
        @Override
        public String convertToDatabaseColumn(TransactionDirection direction) {
            return direction.name();
        }

        @Override
        public TransactionDirection convertToEntityAttribute(String dbData) {
            return TransactionDirection.valueOf(dbData);
        }
    }
}