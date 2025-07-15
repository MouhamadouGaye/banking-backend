package com.mgaye.banking_backend.service;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.UUID;

import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.response.UserResponse;
import com.mgaye.banking_backend.model.User;

public interface UserService {
    User createUser(RegisterRequest request);

    UserResponse getUserDtoById(UUID userId);

    boolean existsByEmail(String email);

    UserResponse getCurrentUser(UUID userId);

    Optional<User> findById(UUID userId);
}