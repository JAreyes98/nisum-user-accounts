package com.jreyes.accountsapi.service;

import com.jreyes.accountsapi.dto.PhoneRequestDto;
import com.jreyes.accountsapi.dto.PhoneResponseDto;

import java.util.List;

public interface UserPhoneService {
    List<PhoneResponseDto> findAll(String userId);
    PhoneResponseDto findById(String id, String userId);
    PhoneResponseDto save(String userId, PhoneRequestDto phoneRequest);
    PhoneResponseDto update(String userId, String phoneId, PhoneRequestDto phoneRequest);
    void deleteById(String userId, String phoneId);
}
