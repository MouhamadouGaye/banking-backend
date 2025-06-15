package com.mgaye.banking_backend.controller;

import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.request.TokenRefreshRequest;
<<<<<<< HEAD

import com.mgaye.banking_backend.dto.request.LoginRequest;
import com.mgaye.banking_backend.dto.response.TokenRefreshResponse;
import com.mgaye.banking_backend.exception.TokenRefreshException;
import com.mgaye.banking_backend.dto.response.JwtResponse;
import com.mgaye.banking_backend.dto.response.MessageResponse;

import com.mgaye.banking_backend.model.RefreshToken;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.security.jwt.JwtUtils;
import com.mgaye.banking_backend.security.service.UserDetailsImpl;
import com.mgaye.banking_backend.service.RefreshTokenService;
import com.mgaye.banking_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
=======
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

>>>>>>> master
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
<<<<<<< HEAD
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
            JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId()); // Now accepts String

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                refreshToken.getToken(),
                userDetails.getUserId(), // String ID
                userDetails.getEmail(),
                userDetails.getFirstName() + "" + userDetails.getLastName(),
                userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
        }

        User user = userService.createUser(registerRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        refreshTokenService.deleteByUserId(userDetails.getId());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
=======
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
>>>>>>> master
    }
}