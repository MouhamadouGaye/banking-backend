package com.mgaye.banking_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mgaye.banking_backend.client.DocumentVerificationClient;
import com.mgaye.banking_backend.client.SanctionCheckClient;
import com.mgaye.banking_backend.mock.MockDocumentVerificationClient;
import com.mgaye.banking_backend.mock.MockSanctionCheckClient;

@Configuration
public class ClientConfig {

    @Bean
    @Profile("!prod")
    public DocumentVerificationClient mockDocumentVerificationClient() {
        return new MockDocumentVerificationClient();
    }

    @Bean
    @Profile("prod")
    public DocumentVerificationClient acmeDocumentVerificationClient(RestTemplate restTemplate) {
        return new AcmeDocumentVerificationClient(restTemplate);
    }

    @Bean
    @Profile("!prod")
    public SanctionCheckClient mockSanctionCheckClient() {
        return new MockSanctionCheckClient();
    }

    @Bean
    @Profile("prod")
    public SanctionCheckClient worldCheckSanctionClient(RestTemplate restTemplate) {
        return new WorldCheckSanctionClient(restTemplate);
    }
}