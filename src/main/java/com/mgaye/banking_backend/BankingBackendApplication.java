package com.mgaye.banking_backend;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

// @EnableJpaRepositories(basePackages = "com.mgaye.banking_backend.repository")
@SpringBootApplication
// @EntityScan(basePackages = "com.mgaye.banking_backend.model")
public class BankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingBackendApplication.class, args);

	}

}
