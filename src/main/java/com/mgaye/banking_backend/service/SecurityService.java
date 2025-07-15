package com.mgaye.banking_backend.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;

import com.mgaye.banking_backend.dto.request.SecurityUpdateRequest;
import com.mgaye.banking_backend.dto.response.CurrentUser;
import com.mgaye.banking_backend.model.User;

public interface SecurityService {
    void updatePassword(UUID userId, String currentPassword, String newPassword);

    void enableTwoFactorAuth(UUID userId);

    void disableTwoFactorAuth(UUID userId);

    void updateSecurityQuestions(UUID userId, SecurityUpdateRequest request);

    CurrentUser getCurrentUserDetails(UUID userId);

    void logSecurityEvent(UUID userId, String eventType, String description);

    User getUserById(UUID userId);
}