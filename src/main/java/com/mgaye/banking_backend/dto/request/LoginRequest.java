package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// // LoginRequest.java

// // @Data removed because records provide necessary methods automatically
// public record LoginRequest(
//                 @NotBlank @Email String email,
//                 @NotBlank String password,
//                 String deviceId,
//                 String ipAddress) {
// }

import lombok.Data;

@Data
public class LoginRequest {
        @NotBlank
        private String email;

        @NotBlank
        private String password;
}