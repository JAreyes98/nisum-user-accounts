package com.jreyes.accountsapi.repository;

import com.jreyes.accountsapi.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, String> {

    List<Phone> findByUserId(String userId);
    Optional<Phone> findByIdAndUserId(String id, String userId);
}
