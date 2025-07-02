package com.mgaye.banking_backend.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

// @Data
// public class SecurityUpdateRequest {

//     @NotBlank
//     @Size(min = 1, max = 255)
//     private String question1;

//     @NotBlank
//     @Size(min = 1, max = 255)
//     private String answer1;

//     @NotBlank
//     @Size(min = 1, max = 255)
//     private String question2;

//     @NotBlank
//     @Size(min = 1, max = 255)
//     private String answer2;

//     @NotBlank
//     @Size(min = 1, max = 255)
//     private String question3;

//     @NotBlank
//     @Size(min = 1, max = 255)
//     private String answer3;
// }

// SecurityUpdateRequest.java
@Data
public class SecurityUpdateRequest {
    private String currentPassword; // Optional for some flows
    @NotEmpty
    private List<SecurityQuestionDTO> questions;

    @Data
    public static class SecurityQuestionDTO {
        @NotBlank
        private String question;
        @NotBlank
        private String answer;
    }
}
