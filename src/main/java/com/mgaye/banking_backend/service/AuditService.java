package com.mgaye.banking_backend.service;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;

import com.mgaye.banking_backend.dto.response.AuthResponse;
import com.mgaye.banking_backend.model.User;

// public interface AuthService {
//     Authentication authenticate(String email, String password);

//     AuthResponse createAuthSession(User user);
// }

public interface AuditService {
    void logSecurityEvent(String userId, String eventType, String description);
}