package com.jreyes.accountsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PhoneRequestDto(
        String id,
        @NotBlank(message = "Phone number is required")
        @Size(max = 14, message = "Maximum phone number length is 14 characters")
        String number,
        @NotBlank(message = "City code is required")
        @Size(max = 3, min = 1, message = "City code must have between 1 and 3 characters")
        String cityCode,
        @NotBlank(message = "Country code must have between 1 and 3 characters")
        @Size(max = 3, min = 1, message = "Country code must have between 1 and 3 characters")
        String countryCode
) {
}