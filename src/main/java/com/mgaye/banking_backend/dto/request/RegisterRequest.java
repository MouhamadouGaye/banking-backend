package com.mgaye.banking_backend.dto.request;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

// // RegisterRequest.java
// public record RegisterRequest(
//         @NotBlank @Size(min = 2, max = 50) String firstName,
//         @NotBlank @Size(min = 2, max = 50) String lastName,
//         @NotBlank @Email String email,
//         @NotBlank @Size(min = 8, max = 100) String password,
//         @NotBlank @Pattern(regexp = "^\\+?[0-9]{8,15}$") String phone,
//         @Past LocalDate dob,
//         @Valid AddressRequest address) {
// }

// RegisterRequest.java with complete validation

public record RegisterRequest(
                @NotBlank(message = "First name is required") @Size(min = 2, max = 50, message = "First name must be between 2-50 characters") String firstName,

                @NotBlank(message = "Last name is required") @Size(min = 2, max = 50, message = "Last name must be between 2-50 characters") String lastName,

                @NotBlank(message = "Email is required") @Email(message = "Email should be valid") @Size(max = 100, message = "Email cannot exceed 100 characters") String email,

                @NotBlank(message = "Password is required") @Size(min = 8, max = 100, message = "Password must be 8-100 characters") @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain digit, lowercase, uppercase, special character") String password,

                @NotBlank(message = "Phone number is required") @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Phone number must be 8-15 digits with optional +") String phone,

                @NotNull(message = "Date of birth is required") @Past(message = "Date of birth must be in the past") LocalDate dob,

                @NotNull(message = "Address is required") @Valid AddressRequest address) {
}
