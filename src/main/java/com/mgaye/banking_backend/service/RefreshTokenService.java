package com.mgaye.banking_backend.service;

import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.exception.TokenRefreshException;
import com.mgaye.banking_backend.model.RefreshToken;
import com.mgaye.banking_backend.repository.RefreshTokenRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

// @Service
// @RequiredArgsConstructor
// public class RefreshTokenService {
//     private final RefreshTokenRepository refreshTokenRepository;
//     private final UserRepository userRepository;

//     @Value("${app.jwt.refreshExpirationMs}")
//     private Long refreshTokenDurationMs;

//     public RefreshToken createRefreshToken(String userId) {
//         RefreshToken refreshToken = new RefreshToken();
//         refreshToken.setUser(userRepository.findById(userId).orElseThrow(
//                 () -> new RuntimeException("User not found")));
//         refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
//         refreshToken.setToken(UUID.randomUUID().toString());
//         return refreshTokenRepository.save(refreshToken);
//     }

//     public RefreshToken verifyExpiration(RefreshToken token) {
//         if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
//             refreshTokenRepository.delete(token);
//             throw new TokenRefreshException(token.getToken(), "Refresh token was expired");
//         }
//         return token;
//     }

//     @Transactional
//     public void deleteByUserId(String userId) {
//         refreshTokenRepository.deleteByUser(userRepository.findById(userId).orElseThrow(
//                 () -> new RuntimeException("User not found")));
//     }
// }

// @Service
// @RequiredArgsConstructor
// public class RefreshTokenService {

//     @Value("${app.jwt.refreshExpirationMs}")
//     private Long refreshTokenDurationMs;

//     private final RefreshTokenRepository refreshTokenRepository;
//     private final UserRepository userRepository;

//     public Optional<RefreshToken> findByToken(String token) {
//         return refreshTokenRepository.findByToken(token);
//     }

//     public RefreshToken createRefreshToken(Long userId) {
//         RefreshToken refreshToken = new RefreshToken();

//         refreshToken.setUser(userRepository.findById(userId).orElseThrow(
//                 () -> new ResourceNotFoundException("User not found with id: " + userId)));
//         refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
//         refreshToken.setToken(UUID.randomUUID().toString());

//         refreshToken = refreshTokenRepository.save(refreshToken);
//         return refreshToken;
//     }

//     public RefreshToken verifyExpiration(RefreshToken token) {
//         if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
//             refreshTokenRepository.delete(token);
//             throw new TokenRefreshException(token.getToken(),
//                     "Refresh token was expired. Please make a new signin request");
//         }
//         return token;
//     }

//     @Transactional
//     public void deleteByUserId(Long userId) {
//         refreshTokenRepository.deleteByUser(userRepository.findById(userId).orElseThrow(
//                 () -> new ResourceNotFoundException("User not found with id: " + userId)));
//     }
// }

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${app.jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + userId)));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public void deleteByUserId(String userId) {
        refreshTokenRepository.deleteByUser(userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + userId)));
    }
}