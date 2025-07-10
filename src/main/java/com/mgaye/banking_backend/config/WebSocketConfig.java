// package com.mgaye.banking_backend.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import
// org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
// import
// org.springframework.web.socket.config.annotation.StompEndpointRegistry;
// import
// org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
// import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;
// import org.springframework.messaging.simp.config.ChannelRegistration;
// import org.springframework.messaging.simp.config.MessageBrokerRegistry;
// // import org.springframework.messaging.simp.user.SimpUserRegistry;
// // import org.springframework.messaging.simp.SimpMessageSendingOperations;
// // import org.springframework.messaging.MessageChannel;

// // @Configuration
// // @EnableWebSocketMessageBroker
// // public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

// // @Override
// // public void configureMessageBroker(MessageBrokerRegistry config) {
// // config.enableSimpleBroker("/topic", "/queue", "/user");
// // config.setApplicationDestinationPrefixes("/app");
// // config.setUserDestinationPrefix("/user");
// // }

// // @Override
// // public void registerStompEndpoints(StompEndpointRegistry registry) {
// // registry.addEndpoint("/ws")
// // .setAllowedOriginPatterns("*")
// // .addInterceptors(new AuthHandshakeInterceptor(tokenProvider))
// // .withSockJS()
// // .setHeartbeatTime(30000);
// // }

// // @Override
// // public void configureClientInboundChannel(ChannelRegistration
// registration) {
// // registration.interceptors(new JwtChannelInterceptor(tokenProvider));
// // }

// // @Bean
// // public SimpUserRegistry simpUserRegistry() {
// // return new DefaultSimpUserRegistry();
// // }
// // }