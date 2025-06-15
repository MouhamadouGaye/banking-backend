package com.mgaye.banking_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
<<<<<<< HEAD
=======
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.MessageChannel;
>>>>>>> master

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Bean
<<<<<<< HEAD
    public SimpMessagingTemplate simpMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
        return new SimpMessagingTemplate(messagingTemplate);
    }
}
=======
    public SimpMessagingTemplate simpMessagingTemplate(MessageChannel clientOutboundChannel) {
        return new SimpMessagingTemplate(clientOutboundChannel);
    }
}
>>>>>>> master
