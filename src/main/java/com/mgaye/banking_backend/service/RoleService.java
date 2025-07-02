// service/RoleService.java
package com.mgaye.banking_backend.service;

import java.security.Permission;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.model.Role;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final roleRepository roleRepository;

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
        role.setName(name);
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    @Transactional
    public void assignRoleToUser(String userId, String roleName) {
        // Implementation
    }
}