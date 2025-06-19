package com.mgaye.banking_backend.config;

import org.springframework.context.annotation.Bean;

// OpenAPIConfig.java

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import io.swagger.v3.oas.models.info.License;

import io.swagger.v3.oas.annotations.media.Schema;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Configuration
public class OpenAPIConfig {

        @Bean
        public OpenAPI bankingOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Banking API")
                                                .description("Comprehensive API for modern banking applications")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("API Support Team")
                                                                .email("api-support@banking.com")
                                                                .url("https://banking.com/support"))
                                                .license(new License()
                                                                .name("Banking API License")
                                                                .url("https://banking.com/license")))
                                .externalDocs(new ExternalDocumentation()
                                                .description("Banking API Documentation")
                                                .url("https://docs.banking.com/api"))
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth",
                                                                new SecurityScheme()
                                                                                .name("bearerAuth")
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
        }

        @Schema(description = "User registration request")
        public record RegisterRequest(
                        @Schema(description = "User's first name", example = "John", requiredMode = REQUIRED) @NotBlank String firstName,

                        @Schema(description = "User's last name", example = "Doe", requiredMode = REQUIRED) @NotBlank String lastName,

                        @Schema(description = "User's email address", example = "user@example.com", requiredMode = REQUIRED) @NotBlank @Email String email,

                        @Schema(description = "User's password (8-20 characters)", example = "SecurePass123!", requiredMode = REQUIRED) @NotBlank @Size(min = 8, max = 20) String password) {
        }
}