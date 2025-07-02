package com.mgaye.banking_backend.dto.response;

import com.mgaye.banking_backend.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// AuthResponse.java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private Long expiresIn;
        private UserResponse user;
}