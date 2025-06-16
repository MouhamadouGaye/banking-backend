package com.mgaye.banking_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PinUpdateRequest {

    @jakarta.validation.constraints.NotBlank(message = "Current PIN is required")
    @Size(min = 4, max = 6, message = "PIN must be between 4 and 6 digits")
    @Pattern(regexp = "[0-9]+", message = "PIN must contain only digits")
    private String currentPin;

    @NotBlank(message = "New PIN is required")
    @Size(min = 4, max = 6, message = "PIN must be between 4 and 6 digits")
    @Pattern(regexp = "[0-9]+", message = "PIN must contain only digits")
    private String newPin;
}