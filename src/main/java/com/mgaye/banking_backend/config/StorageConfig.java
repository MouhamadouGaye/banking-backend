package com.mgaye.banking_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.mgaye.banking_backend.service.StorageService;
import com.mgaye.banking_backend.service.impl.LocalStorageService;

@Configuration
public class StorageConfig {

    @Bean
    @Profile("default")
    public StorageService localStorageService(
            @Value("${app.storage.local.path:./storage}") String storagePath) {
        return new LocalStorageService(storagePath);
    }

    // Add other storage implementations (S3, Azure Blob, etc.) with different
    // profiles
}