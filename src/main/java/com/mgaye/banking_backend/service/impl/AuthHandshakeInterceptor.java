// package com.mgaye.banking_backend.service.impl;

// public class AuthHandshakeInterceptor implements HandshakeInterceptor {

// private final JwtTokenProvider tokenProvider;

// @Override
// public boolean beforeHandshake(/* ... */) {
// String token = getTokenFromRequest(request);
// if (!tokenProvider.validateToken(token)) {
// throw new AuthenticationException("Invalid WebSocket token");
// }
// return true;
// }
// }