package com.mgaye.banking_backend.service;

public interface SecurityService {

    void updatePassword(Long userId, String currentPassword, String newPassword);

    void enableTwoFactorAuth(Long userId);

    void disableTwoFactorAuth(Long userId);

    void updateSecurityQuestions(Long userId, SecurityUpdateRequest request);

    CurrentUser getCurrentUserDetails(String username);

    void logSecurityEvent(Long userId, String eventType, String description);
}