package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SecurityUpdateRequest {

    @NotBlank
    @Size(min = 1, max = 255)
    private String question1;

    @NotBlank
    @Size(min = 1, max = 255)
    private String answer1;

    @NotBlank
    @Size(min = 1, max = 255)
    private String question2;

    @NotBlank
    @Size(min = 1, max = 255)
    private String answer2;

    @NotBlank
    @Size(min = 1, max = 255)
    private String question3;

    @NotBlank
    @Size(min = 1, max = 255)
    private String answer3;
}