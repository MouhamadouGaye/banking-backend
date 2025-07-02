package com.mgaye.banking_backend.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.mgaye.banking_backend.dto.request.SecurityUpdateRequest;
import com.mgaye.banking_backend.dto.response.CurrentUser;
import com.mgaye.banking_backend.model.User;

public interface SecurityService {
    void updatePassword(String userId, String currentPassword, String newPassword);

    void enableTwoFactorAuth(String userId);

    void disableTwoFactorAuth(String userId);

    void updateSecurityQuestions(String userId, SecurityUpdateRequest request);

    CurrentUser getCurrentUserDetails(String userId);

    void logSecurityEvent(String userId, String eventType, String description);

    User getUserById(String userId);
}