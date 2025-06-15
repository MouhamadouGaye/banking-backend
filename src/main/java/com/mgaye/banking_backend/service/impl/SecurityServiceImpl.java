package com.mgaye.banking_backend.service.impl;

p

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.dto.response.CurrentUser;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.security.service.UserDetailsImpl;
import com.mgaye.banking_backend.service.AuditService;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    @Override
    @Transactional
    public void updatePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastPasswordChangeDate(java.time.LocalDateTime.now());
        userRepository.save(user);
        
        auditService.logSecurityEvent(userId, "PASSWORD_CHANGE", "User changed password");
    }

    @Override
    @Transactional
    public void enableTwoFactorAuth(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setTwoFactorEnabled(true);
        userRepository.save(user);
        
        auditService.logSecurityEvent(userId, "2FA_ENABLED", "User enabled two-factor authentication");
    }

    @Override
    @Transactional
    public void disableTwoFactorAuth(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setTwoFactorEnabled(false);
        userRepository.save(user);
        
        auditService.logSecurityEvent(userId, "2FA_DISABLED", "User disabled two-factor authentication");
    }

    @Override
    public CurrentUser getCurrentUserDetails(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        return CurrentUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .enabled(user.isEnabled())
                .twoFactorEnabled(user.isTwoFactorEnabled())
                .roles(userDetails.getAuthorities().stream()
                        .map(a -> Role.valueOf(a.getAuthority()))
                        .collect(Collectors.toSet()))
                .lastLogin(user.getLastLogin())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    @Override
    public void logSecurityEvent(Long userId, String eventType, String description) {
        auditService.logSecurityEvent(userId, eventType, description);
    }
}