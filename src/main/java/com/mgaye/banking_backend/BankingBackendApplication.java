package com.mgaye.banking_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.mgaye.banking_backend.config.BankingApiConfig;

// @EnableJpaRepositories(basePackages = "com.mgaye.banking_backend.repository")

@SpringBootApplication
@EnableConfigurationProperties(BankingApiConfig.class)
@EntityScan(basePackages = "com.mgaye.banking_backend.model")
public class BankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingBackendApplication.class, args);
	}

}
