package com.jreyes.accountsapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserRequestDto(

        @NotBlank(message = "Username is required")
        @Size(min = 5, message = "Name must be at least 5 characters long")
        String name,
        @NotBlank(message = "Email is required")
        @Email
        String email,
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._-])(?=\\S+$).+$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character (@#$%^&+=!._-)"
        )
        String password,
        @Valid
        List<PhoneRequestDto> phones
) {
}