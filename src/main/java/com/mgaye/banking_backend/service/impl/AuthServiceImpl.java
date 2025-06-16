package com.mgaye.banking_backend.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.mapper.UserMapper;
import com.mgaye.banking_backend.dto.response.AuthResponse;
import com.mgaye.banking_backend.model.User;
import com.mgaye.banking_backend.security.jwt.JwtUtils;
import com.mgaye.banking_backend.security.service.UserDetailsImpl;
import com.mgaye.banking_backend.service.AuthService;
import com.mgaye.banking_backend.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    @Override
    public Authentication authenticate(String email, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
    }

    @Override
    public AuthResponse createAuthSession(User user) {
        // Create UserDetails for token generation
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        // Generate tokens
        String accessToken = jwtUtils.generateJwtToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getId()).getToken();

        // Build response
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toUserResponse(user))
                .build();
    }
}