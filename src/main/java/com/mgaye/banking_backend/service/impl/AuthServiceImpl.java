package com.mgaye.banking_backend.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.AuthResponse;
import com.mgaye.banking_backend.dto.mapper.UserMapper;
import com.mgaye.banking_backend.dto.request.RegisterRequest;
import com.mgaye.banking_backend.dto.response.TokenRefreshResponse;
import com.mgaye.banking_backend.dto.response.UserResponse;
import com.mgaye.banking_backend.exception.EmailAlreadyExistsException;
import com.mgaye.banking_backend.exception.ResourceNotFoundException;
import com.mgaye.banking_backend.exception.TokenRefreshException;
import com.mgaye.banking_backend.model.RefreshToken;
import com.mgaye.banking_backend.model.Role;
import com.mgaye.banking_backend.model.Role.ERole;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.repository.RoleRepository;
import com.mgaye.banking_backend.repository.UserRepository;
import com.mgaye.banking_backend.security.jwt.JwtUtils;
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

    @Override
    public AuthResponse authenticate(String email, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        User user = (User) auth.getPrincipal();
        return createAuthSession(user);
    }

    @Override
    public AuthResponse createAuthSession(User user) {
        String accessToken = jwtUtils.generateTokenFromUsername(user.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(3600)
                .user(userMapper.toUserResponse(user))
                .build();
    }

    @Override
    @Transactional
    public TokenRefreshResponse refreshToken(String refreshToken) {
        // 1. Find the refresh token in database
        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token not found"));

        // 2. Verify if it's expired
        refreshTokenService.verifyExpiration(token);

        // 3. Generate new access token
        String newAccessToken = jwtUtils.generateTokenFromUsername(token.getUser().getEmail());

        // 4. Return new tokens (keeping same refresh token)
        return new TokenRefreshResponse(newAccessToken, refreshToken);
    }
}
