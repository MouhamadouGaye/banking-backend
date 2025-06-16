package com.mgaye.banking_backend.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.cglib.core.Local;

import com.mgaye.banking_backend.model.Role;
import com.mgaye.banking_backend.model.User.KycStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentUser {
    private String id;
    private String email; // Using as username
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean twoFactorEnabled;
    private boolean loginAlerts; // Added from SecuritySettings
    private Set<Role> roles;
    private String language; // Added from UserSettings
    private ThemePreference theme; // Added from UserSettings
    private String currency; // Added from UserSettings
    private LocalDateTime createdAt;
    private KycStatus kycStatus;
    private LocalDateTime lastLogin; // Not available in your current entities
    private String profileImageUrl;

    // Removed fields not available in your entities:
    // - lastLogin (not in SecuritySettings)
    // - profileImageUrl (not in UserSettings)

    public enum ThemePreference {
        LIGHT, DARK, SYSTEM
    }
}