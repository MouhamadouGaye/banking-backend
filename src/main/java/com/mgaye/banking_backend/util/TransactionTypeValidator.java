package com.mgaye.banking_backend.util;

import com.mgaye.banking_backend.model.Transaction;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// ValidTransactionType.java
public class TransactionTypeValidator implements ConstraintValidator<ValidTransactionType, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            Transaction.TransactionType.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}