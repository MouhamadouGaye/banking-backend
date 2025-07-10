
package com.mgaye.banking_backend.exception;

public class RoleAlreadyAssignedException extends RuntimeException {
    public RoleAlreadyAssignedException(String userId, String roleName) {
        super("User with ID " + userId + " already has the role: " + roleName);
    }
}