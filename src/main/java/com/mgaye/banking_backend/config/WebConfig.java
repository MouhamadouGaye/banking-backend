package com.mgaye.banking_backend.config;

import java.io.IOException;

import javax.naming.ServiceUnavailableException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebConfig {

    @Bean
    public RestTemplate restTemplate(BankingApiConfig apiConfig) {
        RestTemplate restTemplate = new RestTemplate();

        // Use timeout from configuration
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(apiConfig.getTimeoutMillis());
        factory.setReadTimeout(apiConfig.getTimeoutMillis());

        restTemplate.setRequestFactory(factory);

        // Add error handler
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().is5xxServerError()) {
                    log.error("Banking API server error: {}", response.getStatusCode());
                } else if (response.getStatusCode().is4xxClientError()) {
                    log.warn("Banking API client error: {}", response.getStatusCode());
                }
                super.handleError(response);
            }
        });

        // Add API key interceptor
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("X-API-KEY", apiConfig.getApiKey());
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}