// service/RoleService.java
package com.mgaye.banking_backend.service;

import java.security.Permission;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.management.relation.RoleNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgaye.banking_backend.model.Role;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.Role.ERole;
import com.mgaye.banking_backend.repository.RoleRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.exception.RoleAlreadyAssignedException;
import com.mgaye.banking_backend.exception.RoleAlreadyExistsException;
import com.mgaye.banking_backend.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role createRole(String name, Set<Permission> permissions) {
        if (roleRepository.existsByName(name)) {
            throw new RoleAlreadyExistsException(name);
        }

        Role role = new Role();
        role.setName(Role.ERole.ROLE_USER);
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    // @Transactional
    // public void assignRoleToUser(String userId, String roleName) throws
    // RoleNotFoundException {
    // // 1. Validate input
    // if (userId == null || userId.isBlank()) {
    // throw new IllegalArgumentException("User ID cannot be null or empty");
    // }
    // if (roleName == null || roleName.isBlank()) {
    // throw new IllegalArgumentException("Role name cannot be null or empty");
    // }

    // // 2. Find the role
    // Role role = roleRepository.findByName(roleName)
    // .orElseThrow(() -> new RoleNotFoundException(roleName));

    // // 3. Find the user (assuming you have a UserRepository)
    // User user = userRepository.findById(userId)
    // .orElseThrow(() -> new UserNotFoundException(userId));

    // // 4. Check if user already has this role
    // if (user.getRoles().contains(role)) {
    // throw new RoleAlreadyAssignedException(userId, roleName);
    // }

    // // 5. Assign the role
    // user.getRoles().add(role);
    // userRepository.save(user); // Save the updated user entity
    // }

    @Transactional
    public void assignRoleToUser(String userId, ERole roleName) {
        // 1. Validate input
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (roleName.name() == null || roleName.name().isBlank()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }

        // 2. Find the role
        Role role;
        try {
            role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RoleNotFoundException(roleName.name()));
        } catch (RoleNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 3. Find the user (assuming you have a UserRepository)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // 4. Check if user already has this role
        if (user.getRoles().contains(role)) {
            throw new RoleAlreadyAssignedException(userId, roleName.name());
        }

        // 5. Assign the role
        user.getRoles().add(role);
        userRepository.save(user); // Save the updated user entity
    }
}