package com.mgaye.banking_backend.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mgaye.banking_backend.dto.request.LoginRequest;
import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.response.AuthResponse;
import com.mgaye.banking_backend.dto.response.UserResponse;
import com.mgaye.banking_backend.exception.EmailAlreadyExistsException;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.security.service.UserDetailsImpl;
import com.mgaye.banking_backend.service.AuthService;
import com.mgaye.banking_backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// UserController.java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService; // Changed from AuthorizationServiceException

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }
        User user = userService.createUser(request);
        return ResponseEntity.ok(authService.createAuthSession(user));
    }

    // @PostMapping("/login")
    // public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest
    // request) {
    // Authentication authentication = authService.authenticate(request.getEmail(),
    // request.getPassword());
    // User user = (User) authentication.getPrincipal();
    // return ResponseEntity.ok(authService.createAuthSession(user));
    // }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            // 1. Authenticate and get Spring Security Authentication object
            Authentication authentication = (Authentication) authService.authenticate(request.getEmail(),
                    request.getPassword());

            // 2. Get the principal (should be UserDetails)
            Object principal = authentication.getPrincipal();

            // 3. Handle different principal types
            User user;
            if (principal instanceof UserDetailsImpl) {
                // If using custom UserDetails implementation
                UserDetailsImpl userDetails = (UserDetailsImpl) principal;
                user = userService.findById(userDetails.getId())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            } else if (principal instanceof User) {
                // If principal is directly your User entity
                user = (User) principal;
            } else {
                throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
            }

            // 4. Create auth session
            return ResponseEntity.ok(authService.createAuthSession(user));

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials", e);
        }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserDtoById(userId));
    }
}