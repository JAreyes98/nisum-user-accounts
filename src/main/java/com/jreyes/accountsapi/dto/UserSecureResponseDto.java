package com.jreyes.accountsapi.dto;

import com.jreyes.accountsapi.domain.User;

import java.util.Date;
import java.util.List;

public record UserSecureResponseDto(
        String id,
        String name,
        String email,
        Date createdOn,
        Date updatedOn,
        List<PhoneResponseDto> phones
) {
    /**
     * Mapping method that transform from a User instance into a Response instance.
     * @author jreyes
     * @since 2026
     * Also see {@link PhoneResponseDto}
     */
    public static UserSecureResponseDto fromEntity(User user) {
        return new UserSecureResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedOn(),
                user.getUpdatedOn(),
                user.getPhones()
                        .stream()
                        .map(PhoneResponseDto::fromEntity)
                        .toList()
        );
    }
}
