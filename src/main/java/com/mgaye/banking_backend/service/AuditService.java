package com.mgaye.banking_backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;

import com.mgaye.banking_backend.dto.response.AuthResponse;
import com.mgaye.banking_backend.model.AuditLog;
import com.mgaye.banking_backend.model.Transaction;
import com.mgaye.banking_backend.model.User;

// public interface AuthService {
//     Authentication authenticate(String email, String password);

//     AuthResponse createAuthSession(User user);
// }

public interface AuditService {
    void logSecurityEvent(String userId, String eventType, String description);

    void logTransaction(Transaction transaction, User user);

    void logTransactionCancellation(Transaction transaction, User user);

    void recordAccountStatusChange(UUID accountId, String newStatus, String reason, User user);

}
