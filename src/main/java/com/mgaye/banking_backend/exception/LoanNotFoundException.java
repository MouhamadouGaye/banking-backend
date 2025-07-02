package com.mgaye.banking_backend.exception;

import java.util.UUID;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(UUID loanId) {
        super("Loan not found: " + loanId);
    }
}
