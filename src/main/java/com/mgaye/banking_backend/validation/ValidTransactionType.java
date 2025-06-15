package com.mgaye.banking_backend.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.Payload;

// validation/ValidTransactionType.java
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = TransactionTypeValidator.class)
public @interface ValidTransactionType {
    String message() default "Invalid transaction type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

// validation/TransactionTypeValidator.java
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