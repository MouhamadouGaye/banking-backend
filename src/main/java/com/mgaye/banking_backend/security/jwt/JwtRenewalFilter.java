package com.mgaye.banking_backend.security.jwt;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// security/jwt/JwtRenewalFilter.java
public class JwtRenewalFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) {
        String token = jwtUtils.parseJwt(request);
        if (jwtUtils.shouldRenew(token)) {
            String newToken = jwtUtils.renewToken(token);
            response.setHeader("X-Renewed-Token", newToken);
        }
        chain.doFilter(request, response);
    }
}