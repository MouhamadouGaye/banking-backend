package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.dto.request.RegisterRequest;
<<<<<<< HEAD
import com.mgaye.banking_backend.model.Role;
import com.mgaye.banking_backend.model.SecuritySettings;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.model.UserSettings;
import com.mgaye.banking_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.*;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createUser(RegisterRequest registerRequest) {
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .build();

        user.setRoles(Set.of(roleService.findByName(Role.ERole.ROLE_USER)));

        // Create default settings
        UserSettings settings = UserSettings.builder()
                .user(user)
                .theme(UserSettings.Theme.SYSTEM)
                .language("en")
                .currency("USD")
                .build();
        user.setUserSettings(settings);

        // Create default security settings
        SecuritySettings securitySettings = SecuritySettings.builder()
                .user(user)
                .twoFactorEnabled(false)
                .loginAlerts(true)
                .build();
        user.setSecuritySettings(securitySettings);

        return userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
=======
import com.mgaye.banking_backend.dto.response.UserResponse;
import com.mgaye.banking_backend.model.User;

public interface UserService {
    User createUser(RegisterRequest request);

    UserResponse getUserDtoById(String userId);

    boolean existsByEmail(String email);

    UserResponse getCurrentUser(String userId);
>>>>>>> master
}