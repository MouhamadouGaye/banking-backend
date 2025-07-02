package com.mgaye.banking_backend.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.checkerframework.framework.qual.TargetLocations;

import java.lang.annotation.ElementType;

import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.util.TransactionTypeValidator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransactionTypeValidator.class)
public @interface ValidTransactionType {
    String message() default "Invalid transaction type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

// TransactionTypeValidator is now in its own file.