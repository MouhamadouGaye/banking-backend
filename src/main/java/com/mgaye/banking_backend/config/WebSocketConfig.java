// package com.mgaye.banking_backend.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
// import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
// import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
// import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;
// import org.springframework.messaging.simp.config.ChannelRegistration;
// import org.springframework.messaging.simp.config.MessageBrokerRegistry;
// import org.springframework.messaging.simp.user.SimpUserRegistry;
// import org.springframework.messaging.simp.SimpMessageSendingOperations;
// import org.springframework.messaging.MessageChannel;

// @Configuration
// @EnableWebSocketMessageBroker
// public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//     @Override
//     public void configureMessageBroker(MessageBrokerRegistry config) {
//         config.enableSimpleBroker("/topic", "/queue", "/user");
//         config.setApplicationDestinationPrefixes("/app");
//         config.setUserDestinationPrefix("/user");
//     }

//     @Override
//     public void registerStompEndpoints(StompEndpointRegistry registry) {
//         registry.addEndpoint("/ws")
//                 .setAllowedOriginPatterns("*")
//                 .addInterceptors(new AuthHandshakeInterceptor(tokenProvider))
//                 .withSockJS()
//                 .setHeartbeatTime(30000);
//     }

//     @Override
//     public void configureClientInboundChannel(ChannelRegistrationregistration) {
//     registration.interceptors(new JwtChannelInterceptor(tokenProvider));
//     }

//     @Bean
//     public SimpUserRegistry simpUserRegistry() {
//         return new DefaultSimpUserRegistry();
//     }
// }

package com.mgaye.banking_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(jacksonMessageConverter());
        return true;
    }

    @Bean
    public MappingJackson2MessageConverter jacksonMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        // Set any custom ObjectMapper configuration if needed
        return converter;
    }

    // Rest of your WebSocket configuration remains the same
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }
}