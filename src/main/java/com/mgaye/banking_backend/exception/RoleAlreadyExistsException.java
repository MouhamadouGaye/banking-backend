package com.mgaye.banking_backend.exception;

public class RoleAlreadyExistsException extends RuntimeException {
    public RoleAlreadyExistsException(String roleName) {
        super("Role already exists: " + roleName);
    }
}