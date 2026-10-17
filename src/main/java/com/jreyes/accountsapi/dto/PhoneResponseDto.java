package com.jreyes.accountsapi.dto;

import com.jreyes.accountsapi.domain.Phone;

public record PhoneResponseDto (
        String id,
        String number,
        String cityCode,
        String countryCode
) {
    public static PhoneResponseDto fromEntity(Phone phone) {
        return new PhoneResponseDto(
                phone.getId(),
                phone.getNumber(),
                phone.getCityCode(),
                phone.getCountryCode()
        );
    }
}
