package com.mgaye.banking_backend.controller;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgaye.banking_backend.dto.AuthResponse;
import com.mgaye.banking_backend.dto.request.LoginRequest;
import com.mgaye.banking_backend.dto.request.RegisterRequest;
<<<<<<< HEAD
=======
import com.mgaye.banking_backend.dto.response.UserResponse;
>>>>>>> master
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// UserController.java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthorizationServiceException authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.createUser(request);
        AuthResponse authResponse = authService.createAuthSession(user);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
<<<<<<< HEAD
        Authentication authentication = authService.authenticate(request.email(), request.password());
=======
        Authentication authentication = authService.authenticate(request.getEmail(), request.getPassword());
>>>>>>> master
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(authService.createAuthSession(user));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserDtoById(userId));
    }
}