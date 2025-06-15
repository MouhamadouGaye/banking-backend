package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.response.UserResponse;
import com.mgaye.banking_backend.model.User;

public interface UserService {
    User createUser(RegisterRequest request);

    UserResponse getUserDtoById(String userId);

    boolean existsByEmail(String email);

    UserResponse getCurrentUser(String userId);
}