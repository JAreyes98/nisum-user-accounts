package com.jreyes.accountsapi.service.impl;

import com.jreyes.accountsapi.config.PasswordConfig;
import com.jreyes.accountsapi.domain.Phone;
import com.jreyes.accountsapi.domain.User;
import com.jreyes.accountsapi.dto.*;
import com.jreyes.accountsapi.exception.ResourceNotFoundException;
import com.jreyes.accountsapi.repository.UserRepository;
import com.jreyes.accountsapi.service.JWTService;
import com.jreyes.accountsapi.service.UserPhoneService;
import com.jreyes.accountsapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * User Service Implementation
 * <p>Contains all features available to manage user accounts</p>
 * @author jreyes
 * @since 2026
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserPhoneService userPhoneService;
    private final PasswordConfig passwordConfig;
    private final JWTService jwtService;


    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UserSecureResponseDto findById(String id) {
        return UserSecureResponseDto.fromEntity(findUserEntityById(id));
    }


    @Transactional
    @Override
    public UserResponseDto save(UserRequestDto userRequest) {
        validateIfExist(userRequest);
        var user = User.fromRequest(userRequest);
        user.setPassword(passwordConfig.passwordEncoder().encode(user.getPassword()));
        user.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);

        user.setToken(jwtService.generateToken(user));
        userRepository.save(user);

        return UserResponseDto.fromEntity(user);
    }

    @Transactional
    @Override
    public UserSecureResponseDto update(String id, UserUpdateRequestDto userRequest) {
        var user = findUserEntityById(id);
        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setPassword(Objects.nonNull(userRequest.password()) ? userRequest.password() : user.getPassword());
        user.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));

        var requestPhoneIds = userRequest.phones().stream()
                .map(PhoneRequestDto::id)
                .filter(phoneId -> Objects.nonNull(phoneId) && !phoneId.trim().isEmpty())
                .toList();

        user.getPhones().removeIf(existingPhone -> !requestPhoneIds.contains(existingPhone.getId()));

        userRequest.phones().forEach(phoneReq -> {
            if (Objects.isNull(phoneReq.id()) || phoneReq.id().trim().isEmpty()) {
                var phone = Phone.fromRequest(phoneReq, user);
                phone.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
                user.addPhone(phone);
            } else {
                user.getPhones().stream()
                        .filter(p -> p.getId().equalsIgnoreCase(phoneReq.id()))
                        .findFirst()
                        .ifPresent(phone -> {
                            phone.setNumber(phoneReq.number());
                            phone.setCityCode(phoneReq.cityCode());
                            phone.setCountryCode(phoneReq.countryCode());
                        });
            }
        });

        var updatedUser = userRepository.save(user);
        return UserSecureResponseDto.fromEntity(updatedUser);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        var user = findUserEntityById(id);
        userRepository.delete(user);
    }

    @Override
    public User getById(String id) throws UsernameNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    private User findUserEntityById(String id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id: %s not found", id))
        );
    }

    private void validateIfExist(UserRequestDto userRequest) {
        if (userRepository.findUserByEmail(userRequest.email()).isPresent()){
            throw new RuntimeException(String.format("User with email %s already exists", userRequest.email()));
        }
    }
}
