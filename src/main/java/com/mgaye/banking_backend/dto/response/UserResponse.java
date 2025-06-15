package com.mgaye.banking_backend.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import com.mgaye.banking_backend.model.Role;

<<<<<<< HEAD
import lombok.Data;

@Data
=======
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
>>>>>>> master
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean emailVerified;
    private boolean phoneVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
<<<<<<< HEAD
    private Set<Role> roles;
    private boolean active;
=======
    private String tokenType = "Bearer";
    private Set<Role> roles;
    private boolean active;
    private String kycStatus; // Added to match your mapper

>>>>>>> master
}