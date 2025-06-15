package com.mgaye.banking_backend.controller;

import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.request.TokenRefreshRequest;
import com.mgaye.banking_backend.dto.error.ErrorResponse;
import com.mgaye.banking_backend.dto.request.LoginRequest;
import com.mgaye.banking_backend.dto.response.TokenRefreshResponse;
import com.mgaye.banking_backend.dto.response.UserResponse;
import com.mgaye.banking_backend.dto.response.AuthResponse;
import com.mgaye.banking_backend.dto.response.MessageResponse;

import com.mgaye.banking_backend.model.User;

import com.mgaye.banking_backend.security.service.UserDetailsImpl;
import com.mgaye.banking_backend.service.AuthService;
import com.mgaye.banking_backend.service.RefreshTokenService;
import com.mgaye.banking_backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.email())) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("EMAIL_EXISTS", "Email is already registered"));
        }
        User user = userService.createUser(request);
        return ResponseEntity.ok(authService.createAuthSession(user));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.refreshToken()));
    }

    @PostMapping("/signout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponse> logoutUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        refreshTokenService.deleteByUserId(((UserDetailsImpl) authentication.getPrincipal()).getId());
        return ResponseEntity.ok(new MessageResponse("Log out successful"));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(userService.getCurrentUser(((UserDetailsImpl) authentication.getPrincipal()).getId()));
    }
}