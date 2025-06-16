package com.mgaye.banking_backend.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.mapper.UserMapper;
import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.response.UserResponse;
import com.mgaye.banking_backend.exception.EmailAlreadyExistsException;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.model.Role;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.Role.ERole;
import com.mgaye.banking_backend.repository.RoleRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User createUser(RegisterRequest request) {
        if (existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = userMapper.fromRegisterRequest(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_USER.name())

                .orElseThrow(() -> new RuntimeException("Error: Role not found.")));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public UserResponse getUserDtoById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override

    public UserResponse getCurrentUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toUserResponse(user);
    }

    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }

}
