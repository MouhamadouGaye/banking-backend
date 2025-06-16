package com.mgaye.banking_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SecurityQuestionDTO {
    @NotBlank
    private String question;

    @NotBlank
    private String answer;
}