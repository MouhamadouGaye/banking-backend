
package com.mgaye.banking_backend.exception;

import java.util.UUID;

public class RoleAlreadyAssignedException extends RuntimeException {
    public RoleAlreadyAssignedException(UUID userId, String roleName) {
        super("User with ID " + userId + " already has the role: " + roleName);
    }
}