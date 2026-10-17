package com.jreyes.accountsapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserUpdateRequestDto(

        @Size(min = 5, message = "Name must be at least 5 characters long")
        @NotBlank
        String name,
        @Email
        @NotBlank
        String email,
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._-])(?=\\S+$).+$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character (@#$%^&+=!._-)"
        )
        String password,
        List<PhoneRequestDto> phones
) {
}