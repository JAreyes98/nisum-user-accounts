package com.jreyes.accountsapi.service.impl;

import com.jreyes.accountsapi.domain.Phone;
import com.jreyes.accountsapi.domain.User;
import com.jreyes.accountsapi.dto.PhoneRequestDto;
import com.jreyes.accountsapi.dto.PhoneResponseDto;
import com.jreyes.accountsapi.exception.ResourceNotFoundException;
import com.jreyes.accountsapi.repository.PhoneRepository;
import com.jreyes.accountsapi.repository.UserRepository;
import com.jreyes.accountsapi.service.UserPhoneService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for managing user phone numbers.
 * <p>
 * This class handles the business logic for CRUD operations associated with phones
 * belonging to a specific user account. It acts as an intermediary between the
 * controller layer and the data persistence layer.
 * </p>
 *
 * @author jreyes
 * @version 1.0
 * @since 2026
 */
@Service
@AllArgsConstructor
public class UserPhoneServiceImpl implements UserPhoneService {

    private final PhoneRepository phoneRepository;
    @Autowired
    private final UserRepository userRepository;

    /**
     * Retrieves all phone numbers associated with a specific user.
     *
     * @param userId the unique identifier of the user whose phones are being retrieved
     * @return a {@link List} of {@link PhoneResponseDto} containing all phone records for the user,
     *         or an empty list if no phones are found
     */
    @Transactional(readOnly = true)
    @Override
    public List<PhoneResponseDto> findAll(String userId) {
        return phoneRepository.findByUserId(userId)
                .stream()
                .map(PhoneResponseDto::fromEntity)
                .toList();
    }

    /**
     * Retrieves a specific phone number belonging to a specific user.
     *
     * @param id     the unique identifier of the user who owns the phone
     * @param userId the unique identifier of the user who owns the phone
     * @return the {@link PhoneResponseDto} details of the requested phone number
     * @throws RuntimeException if the phone or user does not exist
     */
    @Transactional(readOnly = true)
    @Override
    public PhoneResponseDto findById(String id, String userId) {
        return PhoneResponseDto.fromEntity(getPhoneByUserAndPhoneId(userId, id));
    }

    /**
     * Creates and saves a new phone number record for a specific user.
     *
     * @param userId       the unique identifier of the user to link this phone number to
     * @param phoneRequest the {@link PhoneRequestDto} containing the details of the new phone to create
     * @return the saved phone record mapped as a {@link PhoneResponseDto}
     * @throws RuntimeException if the user does not exist or if the phone number already exists.
     */
    @Transactional
    @Override
    public PhoneResponseDto save(String userId, PhoneRequestDto phoneRequest) {
        var user = getUser(userId);
        var phone = Phone.fromRequest(phoneRequest, user);
        phone.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));

        boolean phoneExists = findAll(userId).stream()
                .anyMatch(existent -> existent.cityCode().equalsIgnoreCase(phone.getCityCode())
                        && existent.countryCode().equalsIgnoreCase(phone.getCountryCode())
                        && existent.number().equalsIgnoreCase(phone.getNumber()));

        if(phoneExists) {
            throw new RuntimeException("Phone number already exists");
        }
        return PhoneResponseDto.fromEntity(phoneRepository.save(phone));
    }

    /**
     * Updates an existing phone number record for a specific user.
     *
     * @param userId       the unique identifier of the user who owns the phone
     * @param phoneId      the unique identifier of the phone record to update
     * @param phoneRequest the {@link PhoneRequestDto} containing the updated information
     * @return the updated phone record mapped as a {@link PhoneResponseDto}
     * @throws RuntimeException if the user or phone record to update is not found
     */
    @Transactional
    @Override
    public PhoneResponseDto update(String userId, String phoneId, PhoneRequestDto phoneRequest) {
        var phone = getPhoneByUserAndPhoneId(userId, phoneId);
        phone.setCountryCode(phoneRequest.countryCode());
        phone.setCityCode(phoneRequest.cityCode());
        phone.setNumber(phoneRequest.number());
        phone.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
        return PhoneResponseDto.fromEntity(phoneRepository.save(phone));
    }

    /**
     * Deletes a phone number record from the system by its unique identifier.
     *
     * @param userId  the unique identifier of the user who owns the phone.
     * @param phoneId the unique identifier of the phone record to delete
     * @throws RuntimeException if the user or phone record does not exist
     */
    @Transactional
    @Override
    public void deleteById(String userId, String phoneId) {
        var phone = getPhoneByUserAndPhoneId(userId, phoneId);
        phoneRepository.delete(phone);
    }

    /**
     * Find user by id
     * @param userId search criteria.
     * @return found user.
     * @throws ResourceNotFoundException if wrong userid
     */
    private User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with Id: %s was not found", userId))
                );
    }

    /**
     * Search Phone by user and phone ID
     * @param userId  Criteria User id
     * @param phoneId Criteria Phone id
     * @return Phone Object
     */
    private Phone getPhoneByUserAndPhoneId(String userId, String phoneId) {
        return phoneRepository.findByIdAndUserId(phoneId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with Id: %s was not found", userId))
                );
    }
}
