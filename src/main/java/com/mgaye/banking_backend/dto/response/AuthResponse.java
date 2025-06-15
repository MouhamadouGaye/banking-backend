package com.mgaye.banking_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok annotation for getters/setters
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private UserResponse user;
}

// package com.mgaye.banking_backend.dto.response;

// import lombok.Builder;

// import lombok.Data;

// @Builder
// public class AuthResponse {
// private String accessToken;
// private String refreshToken;
// private UserResponse user;

// // getters and setters
// }