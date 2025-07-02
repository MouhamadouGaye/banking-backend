package com.mgaye.banking_backend.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.mapper.UserMapper;
import com.mgaye.banking_backend.dto.response.AuthResponse;
import com.mgaye.banking_backend.dto.response.TokenRefreshResponse;
import com.mgaye.banking_backend.exception.TokenRefreshException;
import com.mgaye.banking_backend.model.RefreshToken;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.security.jwt.JwtUtils;
import com.mgaye.banking_backend.security.service.UserDetailsImpl;
import com.mgaye.banking_backend.service.AuthService;
import com.mgaye.banking_backend.service.RefreshTokenService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(String email, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
    }

    @Override
    public AuthResponse createAuthSession(User user) {
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        String accessToken = jwtUtils.generateJwtToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toUserResponse(user))
                .build();
    }

    @Override
    @Transactional
    public TokenRefreshResponse refreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetailsImpl userDetails = UserDetailsImpl.build(user);
                    String newAccessToken = jwtUtils.generateJwtToken(userDetails);

                    return TokenRefreshResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(refreshToken) // Optionally generate new refresh token
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token not found"));
    }
}