package com.mgaye.banking_backend.dto.response;

import java.util.Set;

import com.mgaye.banking_backend.model.Role;

import lombok.Data;

@Data
public class CurrentUser {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean twoFactorEnabled;
    private Set<Role> roles;
    private String lastLogin;
    private String profileImageUrl;
}