package com.mgaye.banking_backend.exception;

// public class InvalidTransactionException extends RuntimeException {
//     public InvalidTransactionException(String message) {
//         super(message);
//     }
// }

public class InvalidTransactionException extends RuntimeException {
    /**
     * Specify serialVersionUID for backward compatibility
     */
    private static final long serialVersionUID = 3597320220337691496L;

    public InvalidTransactionException(String msg) {
        super(msg);
    }

    public InvalidTransactionException() {
        super();
    }

}
