package com.mgaye.banking_backend.service.impl;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.dto.SecurityQuestionDTO;
import com.mgaye.banking_backend.dto.request.SecurityUpdateRequest;
import com.mgaye.banking_backend.dto.response.CurrentUser;
import com.mgaye.banking_backend.exception.InvalidPasswordException;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.SecurityQuestion;
import com.mgaye.banking_backend.model.SecuritySettings;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.UserSettings;
import com.mgaye.banking_backend.repository.SecurityQuestionRepository;
import com.mgaye.banking_backend.repository.SecuritySettingsRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.security.service.UserDetailsImpl;
import com.mgaye.banking_backend.service.AuditService;
import com.mgaye.banking_backend.service.SecurityService;

import jakarta.validation.constraints.NotBlank;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;
    private final SecurityQuestionRepository securityQuestionRepository;
    private final SecuritySettingsRepository securitySettingsRepository;

    @Override
    @Transactional
    public void updatePassword(String userId, String currentPassword, String newPassword) {
        User user = getUserById(userId);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        if (currentPassword.equals(newPassword)) {
            throw new InvalidPasswordException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        updateLastPasswordChange(user);
        userRepository.save(user);
        auditService.logSecurityEvent(userId, "PASSWORD_CHANGE", "Password updated");
    }

    private void updateLastPasswordChange(User user) {
        SecuritySettings settings = user.getSecuritySettings();
        if (settings == null) {
            settings = SecuritySettings.builder()
                    .user(user)
                    .build();
            user.setSecuritySettings(settings);
        }
        settings.setLastPasswordChange(Instant.now());
        securitySettingsRepository.save(settings);
    }

    @Override
    @Transactional
    public void enableTwoFactorAuth(String userId) {
        User user = getUserById(userId);
        SecuritySettings settings = user.getSecuritySettings();
        if (settings == null) {
            settings = SecuritySettings.builder()
                    .user(user)
                    .build();
        }
        settings.setTwoFactorEnabled(true);
        securitySettingsRepository.save(settings);
        logSecurityEvent(userId, "2FA_ENABLED", "Two-factor authentication enabled");
    }

    @Override
    @Transactional
    public void disableTwoFactorAuth(String userId) {
        User user = getUserById(userId);
        SecuritySettings settings = user.getSecuritySettings();
        if (settings == null) {
            settings = SecuritySettings.builder()
                    .user(user)
                    .build();
        }
        settings.setTwoFactorEnabled(false);
        securitySettingsRepository.save(settings);
        logSecurityEvent(userId, "2FA_DISABLED", "Two-factor authentication disabled");
    }

    @Override
    @Transactional
    public void updateSecurityQuestions(String userId, SecurityUpdateRequest request) {
        User user = getUserById(userId);

        // Verify current password if required
        if (request.getCurrentPassword() != null &&
                !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        // Update security questions
        securityQuestionRepository.deleteByUserId(userId);

        List<SecurityQuestion> questions = request.getQuestions().stream()
                .map(q -> SecurityQuestion.builder()
                        .userId(userId)
                        .question(q.getQuestion())
                        .answer(q.getAnswer())
                        .build())
                .collect(Collectors.toList());

        securityQuestionRepository.saveAll(questions);

        logSecurityEvent(userId, "SECURITY_QUESTIONS_UPDATED", "Security questions updated");
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @Override
    public void logSecurityEvent(String userId, String eventType, String description) {
        auditService.logSecurityEvent(userId, eventType, description);
    }

    @Override
    public CurrentUser getCurrentUserDetails(String userId) {
        // Verify authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }

        // Get user with settings eagerly fetched
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Load settings if not already loaded
        if (user.getSecuritySettings() == null) {
            user.setSecuritySettings(new SecuritySettings());
        }
        if (user.getUserSettings() == null) {
            user.setUserSettings(new UserSettings());
        }

        // Verify the authenticated user matches the requested userId
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!userDetails.getId().equals(userId)) {
            throw new SecurityException("Unauthorized access to user details");
        }

        // Build CurrentUser DTO
        return CurrentUser.builder()
                .id(user.getId())
                .email(user.getEmail()) // Using email as username
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .enabled(user.isEnable())
                .twoFactorEnabled(user.getSecuritySettings().isTwoFactorEnabled())
                .loginAlerts(user.getSecuritySettings().isLoginAlerts()) // From SecuritySettings
                .roles(user.getRoles())
                .lastLogin(null) // Not available in your current entities
                .profileImageUrl(null) // Not available in UserSettings
                .language(user.getUserSettings().getLanguage()) // From UserSettings
                .theme(CurrentUser.ThemePreference.valueOf(user.getUserSettings().getTheme().name())) // From
                                                                                                      // UserSettings
                                                                                                      // with conversion
                .currency(user.getUserSettings().getCurrency()) // From UserSettings
                .createdAt(user.getCreatedAt())
                .kycStatus(user.getKycStatus())
                .build();
    }

}