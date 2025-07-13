// package com.mgaye.banking_backend.config;

// import java.security.KeyFactory;
// import java.security.interfaces.RSAPublicKey;
// import java.security.spec.X509EncodedKeySpec;
// import java.util.Base64;
// import java.util.Set;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Primary;
// import org.springframework.context.annotation.Profile;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.oauth2.jwt.JwtDecoder;
// import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// import com.mgaye.banking_backend.model.Role;
// import com.mgaye.banking_backend.model.User;
// import com.mgaye.banking_backend.model.Role.ERole;
// import com.mgaye.banking_backend.security.service.UserDetailsImpl;

// // src/test/java/com/mgaye/banking_backend/TestConfig.java
// import com.mgaye.banking_backend.model.User;
// import com.mgaye.banking_backend.security.services.UserDetailsImpl;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Primary;
// import org.springframework.context.annotation.Profile;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import
// org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.oauth2.jwt.JwtDecoder;
// import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

// import java.security.KeyFactory;
// import java.security.interfaces.RSAPublicKey;
// import java.security.spec.X509EncodedKeySpec;
// import java.time.LocalDate;
// import java.util.Base64;
// import java.util.Collections;

// @TestConfiguration
// @Profile("test")
// public class TestConfig {

// @Bean
// @Primary
// public UserDetailsService userDetailsService() {
// // Create UserDetailsImpl with all required fields
// UserDetailsImpl userDetails = new UserDetailsImpl(
// "test-id", // id
// "Test", // firstName
// "User", // lastName
// "test@example.com", // email
// "1234567890", // phone
// LocalDate.of(1990, 1, 1), // dob
// User.KycStatus.VERIFIED, // kycStatus
// "password", // password
// Collections.singletonList( // authorities
// new SimpleGrantedAuthority("ROLE_USER")));

// return new UserDetailsService() {
// @Override
// public UserDetails loadUserByUsername(String username) {
// if (username.equals("test@example.com")) {
// return userDetails;
// }
// throw new UsernameNotFoundException("User not found");
// }
// };
// }

// @Bean
// @Primary
// public JwtDecoder jwtDecoder() {
// // Create a mock public key for tests
// String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
// "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl4JzCJgB6U1d7M9z1JtG\n" +
// "FakeKeyForTestingDoNotUseInProduction\n" +
// "-----END PUBLIC KEY-----";

// try {
// String publicKeyContent = publicKeyPem
// .replace("-----BEGIN PUBLIC KEY-----", "")
// .replace("-----END PUBLIC KEY-----", "")
// .replaceAll("\\s", "");

// byte[] keyBytes = Base64.getDecoder().decode(publicKeyContent);
// KeyFactory keyFactory = KeyFactory.getInstance("RSA");
// X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
// RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
// return NimbusJwtDecoder.withPublicKey(publicKey).build();
// } catch (Exception e) {
// throw new RuntimeException("Failed to load test public key", e);
// }
// }
// }