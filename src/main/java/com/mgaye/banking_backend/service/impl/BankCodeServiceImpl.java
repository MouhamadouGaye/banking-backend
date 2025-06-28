package com.mgaye.banking_backend.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.mgaye.banking_backend.dto.response.ValidationResponse;
import com.mgaye.banking_backend.service.BankCodeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankCodeServiceImpl implements BankCodeService {

    private final RestTemplate restTemplate;
    private final BankingApiConfig config;

    @Override
    public boolean isValidInternationalCode(String code) {
        try {
            String url = String.format("%s/swift/validate?code=%s", config.getBaseUrl(), code);
            ValidationResponse response = restTemplate.getForObject(url, ValidationResponse.class);
            return response != null && response.isValid();
        } catch (Exception e) {
            log.error("SWIFT code validation failed for code: {}", maskSensitive(code), e);
            return false;
        }
    }

    @Override
    public boolean validateAccountDetails(String accountNumber, String routingNumber) {
        try {
            String url = String.format("%s/account/validate", config.getBaseUrl());
            Map<String, String> request = Map.of(
                    "accountNumber", accountNumber,
                    "routingNumber", routingNumber);

            ValidationResponse response = restTemplate.postForObject(
                    url,
                    request,
                    ValidationResponse.class);

            return response != null && response.isValid();
        } catch (Exception e) {
            log.error("Account validation failed for account: {}", maskSensitive(accountNumber), e);
            return false;
        }
    }

    @Override
    public ValidationResponse getDetailedValidation(String accountNumber, String routingNumber) {
        try {
            String url = String.format("%s/account/validate/details", config.getBaseUrl());
            Map<String, String> request = Map.of(
                    "accountNumber", accountNumber,
                    "routingNumber", routingNumber);

            return restTemplate.postForObject(url, request, ValidationResponse.class);
        } catch (Exception e) {
            log.error("Detailed validation failed for account: {}", maskSensitive(accountNumber), e);
            return new ValidationResponse(false, null, null,
                    "Validation service unavailable");
        }
    }

    private String maskSensitive(String value) {
        if (value == null || value.length() < 4)
            return "****";
        return "****" + value.substring(value.length() - 4);
    }
}