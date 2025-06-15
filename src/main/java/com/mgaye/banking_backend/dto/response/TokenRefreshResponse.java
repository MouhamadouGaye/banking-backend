// package com.mgaye.banking_backend.dto.response;

// // TokenRefreshResponse.java
// public record TokenRefreshResponse(
//         String accessToken,
//         String refreshToken) {
// }

package com.mgaye.banking_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRefreshResponse {
        private String accessToken;
        private String refreshToken;
}