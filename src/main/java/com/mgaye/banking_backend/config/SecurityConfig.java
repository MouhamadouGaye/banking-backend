// package com.mgaye.banking_backend.config;

// import com.mgaye.banking_backend.security.jwt.AuthTokenFilter;
// import com.mgaye.banking_backend.service.impl.UserDetailsServiceImpl;

// import jakarta.validation.Validator;
// import lombok.RequiredArgsConstructor;

// import javax.sql.DataSource;

// import org.hibernate.validator.internal.util.stereotypes.Lazy;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import org.springframework.security.oauth2.jwt.JwtDecoder;
// import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
// import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

// import java.security.KeyFactory;
// import java.security.interfaces.RSAPublicKey;
// import java.security.spec.X509EncodedKeySpec;
// import java.util.Base64;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// @RequiredArgsConstructor
// public class SecurityConfig {

//     @Value("${jwt.public-key}")
//     private String publicKeyPem;

//     @Lazy
//     @Autowired
//     private UserDetailsServiceImpl userDetailsService;

//     @Bean
//     public AuthTokenFilter authenticationJwtTokenFilter() {
//         return new AuthTokenFilter();
//     }

//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//         authProvider.setUserDetailsService(userDetailsService);
//         authProvider.setPasswordEncoder(passwordEncoder());
//         return authProvider;
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//         return authConfig.getAuthenticationManager();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     // @Bean
//     // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//     // http
//     // .csrf(AbstractHttpConfigurer::disable)
//     // // .exceptionHandling(exception ->
//     // // exception.authenticationEntryPoint(unauthorizedHandle))
//     // .sessionManagement(session ->
//     // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//     // .authorizeHttpRequests(auth -> auth
//     // .requestMatchers("/api/auth/**").permitAll()
//     // .requestMatchers("/api/public/**").permitAll()
//     // .requestMatchers("/api/test/**").permitAll()
//     // .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//     // .requestMatchers("/actuator/health").permitAll()
//     // .anyRequest().authenticated());

//     // http.authenticationProvider(authenticationProvider());
//     // http.addFilterBefore(authenticationJwtTokenFilter(),
//     // UsernamePasswordAuthenticationFilter.class);

//     // return http.build();
//     // }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(AbstractHttpConfigurer::disable)
//                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers(
//                                 "/api/auth/**",
//                                 "/api/public/**",
//                                 "/api/test/**",
//                                 "/swagger-ui/**",
//                                 "/v3/api-docs/**",
//                                 "/actuator/health")
//                         .permitAll()
//                         .anyRequest().authenticated())
//                 .oauth2ResourceServer(oauth2 -> oauth2
//                         .jwt(jwt -> jwt.decoder(jwtDecoder())));

//         http.authenticationProvider(authenticationProvider());
//         http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }

//     // @Bean
//     // SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
//     // http
//     // .securityMatcher("/api/**")
//     // .authorizeHttpRequests(auth -> auth
//     // .requestMatchers("/api/public/**").permitAll()
//     // .requestMatchers("/api/auth/**").permitAll()
//     // .anyRequest().authenticated())
//     // .sessionManagement(session -> session
//     // .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//     // .oauth2ResourceServer(oauth2 -> oauth2
//     // .jwt(jwt -> jwt
//     // .decoder(jwtDecoder())));
//     // return http.build();
//     // }

//     // JwtDecoder jwtDecoder() {
//     // java.security.PublicKey publicKey = loadPublicKey();
//     // if (!(publicKey instanceof java.security.interfaces.RSAPublicKey)) {
//     // throw new IllegalArgumentException("Public key must be an instance of
//     // RSAPublicKey");
//     // }
//     // return NimbusJwtDecoder.withPublicKey((java.security.interfaces.RSAPublicKey)
//     // publicKey).build();
//     // }

//     // private java.security.PublicKey loadPublicKey() {
//     // // TODO: Replace with your actual public key loading logic
//     // // Example: Load from a file, classpath, or environment variable
//     // // Here is a simple placeholder that throws an exception
//     // throw new UnsupportedOperationException("Public key loading not
//     // implemented");
//     // }

//     // @Bean
//     // public Validator validator() {
//     // return new LocalValidatorFactoryBean();
//     // }

//     // @Bean
//     // public MethodValidationPostProcessor methodValidationPostProcessor() {
//     // MethodValidationPostProcessor processor = new
//     // MethodValidationPostProcessor();
//     // processor.setValidator(validator());
//     // return processor;
//     // }

//     // // Add this in your SecurityConfig class
//     // @Bean
//     // public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//     // EntityManagerFactoryBuilder builder,
//     // DataSource dataSource) {
//     // return builder
//     // .dataSource(dataSource)
//     // .packages("com.mgaye.banking_backend.model")
//     // .persistenceUnit("default")
//     // .build();
//     // }

//     JwtDecoder jwtDecoder() {
//         RSAPublicKey publicKey = loadPublicKey();
//         return NimbusJwtDecoder.withPublicKey(publicKey).build();
//     }

//     // ADD THE METHOD HERE - INSIDE THE SecurityConfig CLASS
//     private RSAPublicKey loadPublicKey() {
//         // Replace this with your actual public key loading mechanism
//         String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
//                 "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA...\n" +
//                 "-----END PUBLIC KEY-----";

//         try {
//             String publicKeyContent = publicKeyPem
//                     .replace("-----BEGIN PUBLIC KEY-----", "")
//                     .replace("-----END PUBLIC KEY-----", "")
//                     .replaceAll("\\s", "");

//             byte[] keyBytes = Base64.getDecoder().decode(publicKeyContent);
//             KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//             X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
//             return (RSAPublicKey) keyFactory.generatePublic(keySpec);
//         } catch (Exception e) {
//             throw new RuntimeException("Failed to load public key", e);
//         }
//     }

// }

package com.mgaye.banking_backend.config;

import com.mgaye.banking_backend.security.jwt.AuthTokenFilter;
import com.mgaye.banking_backend.security.service.UserDetailsServiceImpl;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import org.springframework.security.core.userdetails.*;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig { // Remove @RequiredArgsConstructor

        private final AuthTokenFilter authTokenFilter;

        @Autowired
        @Lazy
        public SecurityConfig(
                        AuthTokenFilter authTokenFilter) {
                this.authTokenFilter = authTokenFilter;
        }

        // 1. Remove direct dependency on UserDetailsServiceImpl

        // 2. Change to use UserDetailsService interface
        @Bean
        public DaoAuthenticationProvider authenticationProvider(
                        UserDetailsService userDetailsService // Interface injection
        ) {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // // 4. Simplified security configuration
        // @Bean
        // public SecurityFilterChain filterChain(
        // HttpSecurity http,
        // AuthenticationProvider authenticationProvider,
        // JwtDecoder jwtDecoder // Injected decoder
        // ) throws Exception {
        // http
        // .csrf(AbstractHttpConfigurer::disable)
        // .sessionManagement(session -> session
        // .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // .authorizeHttpRequests(auth -> auth
        // .requestMatchers(
        // "/api/auth/**",
        // "/api/public/**",
        // "/api/test/**",
        // "/swagger-ui/**",
        // "/swagger-ui.html",
        // "/v3/api-docs/**",
        // "/actuator/health")
        // .permitAll()
        // .anyRequest().authenticated())
        // .oauth2ResourceServer(oauth2 -> oauth2
        // .jwt(jwt -> jwt.decoder(jwtDecoder))); // Use injected decoder

        // http.authenticationProvider(authenticationProvider);
        // http.addFilterBefore(
        // authTokenFilter,
        // UsernamePasswordAuthenticationFilter.class);

        // return http.build();
        // }

        @Bean
        public SecurityFilterChain filterChain(
                        HttpSecurity http,
                        AuthenticationProvider authenticationProvider,
                        JwtDecoder jwtDecoder) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(Customizer.withDefaults()) // Enable CORS
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/api/auth/**",
                                                                "/api/public/**",
                                                                "/api/test/**",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/actuator/health")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(
                                                authTokenFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                // // Only enable OAuth2 for authenticated requests
                // http.oauth2ResourceServer(oauth2 -> oauth2
                // .jwt(jwt -> jwt.decoder(jwtDecoder))
                // .authorizeHttpRequest(auth -> auth
                // .anyRequest().authenticated()));
                // Only enable OAuth2 for authenticated requests
                http.oauth2ResourceServer(oauth2 -> oauth2
                                .jwt(jwt -> jwt.decoder(jwtDecoder)));

                return http.build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Frontend URL
                configuration.setAllowedMethods(List.of("*"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

}
