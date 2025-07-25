// package com.mgaye.banking_backend.service.impl;

// import static org.junit.jupiter.api.Assertions.*;

// import com.github.tomakehurst.wiremock.WireMockServer;
// import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
// import com.mgaye.banking_backend.exception.SmsException;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;

// @SpringBootTest
// @ActiveProfiles("test")
// class TwilioSmsProviderIntegrationTest {

// @Autowired
// private TwilioSmsProvider twilioSmsProvider;

// private WireMockServer wireMockServer;

// @BeforeEach
// void setUp() {
// wireMockServer = new
// WireMockServer(WireMockConfiguration.options().dynamicPort());
// wireMockServer.start();

// // Configure system properties for Twilio client
// System.setProperty("twilio.account-sid", "test-sid");
// System.setProperty("twilio.auth-token", "test-token");
// System.setProperty("twilio.base-url", "http://localhost:" +
// wireMockServer.port());
// }

// @AfterEach
// void tearDown() {
// wireMockServer.stop();
// // Clear system properties
// System.clearProperty("twilio.account-sid");
// System.clearProperty("twilio.auth-token");
// System.clearProperty("twilio.base-url");
// }

// @Test
// void sendSms_success() {
// // Mock Twilio response
// wireMockServer.stubFor(post(urlPathEqualTo("/2010-04-01/Accounts/test-sid/Messages.json"))
// .willReturn(aResponse()
// .withStatus(201)
// .withHeader("Content-Type", "application/json")
// .withBody("{\"sid\": \"SM1234567890\"}")));

// assertDoesNotThrow(() -> twilioSmsProvider.sendSms("+1234567890", "Test
// message", "+15551234567"));
// }
// }