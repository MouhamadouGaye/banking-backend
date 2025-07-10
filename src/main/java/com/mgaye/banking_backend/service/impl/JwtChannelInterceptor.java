// package com.mgaye.banking_backend.service.impl;

// import org.springframework.messaging.Message;
// import org.springframework.messaging.MessageChannel;
// import org.springframework.messaging.simp.stomp.StompCommand;
// import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
// import org.springframework.messaging.support.ChannelInterceptor;

// public class JwtChannelInterceptor implements ChannelInterceptor {

// private final TokenProvider tokenProvider;

// public JwtChannelInterceptor(TokenProvider tokenProvider) {
// this.tokenProvider = tokenProvider;
// }

// @Override
// public Message<?> preSend(Message<?> message, MessageChannel channel) {
// StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
// if (StompCommand.CONNECT.equals(accessor.getCommand())) {
// String token = accessor.getFirstNativeHeader("Authorization");
// if (!tokenProvider.validateToken(token)) {
// throw new AuthenticationException("Unauthorized");
// }
// }
// return message;
// }
// }