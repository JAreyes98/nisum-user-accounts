package com.jreyes.accountsapi.service;

import com.jreyes.accountsapi.domain.User;
import com.jreyes.accountsapi.dto.UserRequestDto;
import com.jreyes.accountsapi.dto.UserResponseDto;
import com.jreyes.accountsapi.dto.UserSecureResponseDto;
import com.jreyes.accountsapi.dto.UserUpdateRequestDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> findAll();
    UserSecureResponseDto findById(String id);
    UserResponseDto save(UserRequestDto userRequest);
    UserSecureResponseDto update(String id, UserUpdateRequestDto userRequest);
    void deleteById(String id);

    User getById(String id);
}
