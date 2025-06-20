package com.mgaye.banking_backend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.UUID;

public class AdminUserDetails implements UserDetails {
    private final UUID userId;
    // ... other UserDetails implementations ...

    public AdminUserDetails(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        // TODO: return the actual username
        return null;
    }

    @Override
    public String getPassword() {
        // TODO: return the actual password
        return null;
    }

    @Override
    public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: return the actual authorities
        return java.util.Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}