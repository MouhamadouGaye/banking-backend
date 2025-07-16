package com.mgaye.banking_backend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.UUID;

public class AdminUserDetails implements UserDetails {
    private final UUID userId;
    private final String email;
    private final String password;
    // ... other UserDetails implementations ...

    public AdminUserDetails(UUID userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return email; // You should add this field
    }

    @Override
    public String getPassword() {
        return password; // And this too
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