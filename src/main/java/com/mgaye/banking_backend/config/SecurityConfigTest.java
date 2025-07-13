// // src/test/java/com/mgaye/banking_backend/SecurityConfigTest.java
// package com.mgaye.banking_backend.config;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.oauth2.jwt.JwtDecoder;
// import org.springframework.test.context.ActiveProfiles;

// import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
// @ActiveProfiles("test")
// class SecurityConfigTest {

// @Autowired
// private UserDetailsService userDetailsService;

// @Autowired
// private JwtDecoder jwtDecoder;

// @Test
// void testUserDetailsService() {
// UserDetails userDetails =
// userDetailsService.loadUserByUsername("test@example.com");
// assertNotNull(userDetails);
// assertEquals("test@example.com", userDetails.getUsername());
// }

// @Test
// void testJwtDecoder() {
// assertNotNull(jwtDecoder);
// }
// }