package com.mgaye.banking_backend.security.jwt;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// security/jwt/JwtRenewalFilter.java
@RequiredArgsConstructor
public class JwtRenewalFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        // String token = jwtUtils.parseJwt(request);
        // if (jwtUtils.shouldRenew(token)) {
        // String newToken = jwtUtils.renewToken(token);
        // response.setHeader("X-Renewed-Token", newToken);
        // }
        // chain.doFilter(request, response);

        String token = jwtUtils.parseJwt(request);
        if (token != null && jwtUtils.shouldRenew(token)) {
            String newToken = jwtUtils.renewToken(token);
            if (newToken != null) {
                response.setHeader("X-Renewed-Token", newToken);
            }
        }
        chain.doFilter(request, response);

    }
}