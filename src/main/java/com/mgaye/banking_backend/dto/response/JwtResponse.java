// package com.mgaye.banking_backend.dto.response;

// import lombok.*;
// import java.util.List;

// // public record JwtResponse(
// //         String token,
// //         String refreshToken,
// //         String id,
// //         String username,
// //         String email,
// //         List<String> roles) {
// //     // Builder pattern alternative
// //     public static JwtResponse of(String token, String refreshToken, String id,
// //             String username, String email, List<String> roles) {
// //         return new JwtResponse(token, refreshToken, id, username, email, roles);
// //     }
// // }

// @Data
// public class JwtResponse {
//     private String token;
//     private String type = "Bearer";
//     private String refreshToken;
//     private String id;
//     private String email;
//     private String firstName;
//     private String lastName;
//     private String phone;
//     private List<String> roles;

//     public JwtResponse(String token, String refreshToken, String id, String email,
//             String firstName, String lastName, String phone, List<String> roles) {
//         this.token = token;
//         this.refreshToken = refreshToken;
//         this.id = id;
//         this.email = email;
//         this.firstName = firstName;
//         this.lastName = lastName;
//         this.phone = phone;
//         this.roles = roles;
//     }
// }

package com.mgaye.banking_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// @Data
// @NoArgsConstructor
// @AllArgsConstructor // Lombok generates constructor with all fields
// public class JwtResponse {
//     private String token;
//     private String type = "Bearer";
//     private String refreshToken;
//     private String id;
//     private String email;
//     private String firstName;
//     private String lastName;
//     private String phone;
//     private List<String> roles;

//     public JwtResponse(String token, String refreshToken, String id, String email,
//             String firstName, String lastName, String phone,
//             Collection<? extends GrantedAuthority> authorities) {
//         this.token = token;
//         this.refreshToken = refreshToken;
//         this.id = id;
//         this.email = email;
//         this.firstName = firstName;
//         this.lastName = lastName;
//         this.phone = phone;
//         this.roles = authorities.stream()
//                 .map(GrantedAuthority::getAuthority)
//                 .collect(Collectors.toList());
//     }
// }
@Data
@NoArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    String email;
    private String id; // Only ID instead of full user data
    private String tokenType = "Bearer";
    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, String email,
            String userId, Collection<? extends GrantedAuthority> authorities) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = userId;
        this.email = email;
        this.roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}