// package com.mgaye.banking_backend;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.context.annotation.Import;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;

// @SpringBootTest
// @ActiveProfiles("test")
// @AutoConfigureMockMvc
// @Import(TestConfiguration.class)
// class BankingBackendApplicationTests {

// @Autowired
// private MockMvc mockMvc;

// @Test
// @WithMockUser(username = "test@example.com", roles = { "USER" })
// void contextLoadsWithSecurity() throws Exception {
// mockMvc.perform(get("/api/secure-endpoint"))
// .andExpect(status().isOk());
// }

// @Test
// void testPublicEndpoint() throws Exception {
// mockMvc.perform(get("/api/public/info"))
// .andExpect(status().isOk());
// }

// }
