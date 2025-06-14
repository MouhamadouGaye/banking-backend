package com.mgaye.banking_backend.config;

import org.springframework.context.annotation.Bean;

// OpenAPIConfig.java
@Configuration
public class OpenAPIConfig {

    @Bean
                penAPI bankingOp
                        ew OpenAPI()
                        o(new Info()
                        .title("Banking
                        .description("API for 
                                sion("1.0")
                                tact(new Contact()
                        .name("API Support")
                            .email("support@banking.com")))
                        ernalDocs(new ExternalDocumentati
                    .description("Banking API Documentation")
                    .url("https://docs.banki
                        SecurityItem(new SecurityRequirem
                                nts(new Components()
                                        ritySchemes("bearer
                                        SecurityScheme()
                                        .name("bearerAuth
                                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}

// Annotated DTO Example
        ema(description = "User registration request") ic record RegisterRequest(

        @NotBlank String firstName, 
@Schema(description = "User's email", example = "user@example.com", requiredMode = REQUIRED)
   
 @NotBlank @Email String email
    // ... other fields
) {}