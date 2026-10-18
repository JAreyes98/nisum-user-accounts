package com.jreyes.accountsapi.domain;

import com.jreyes.accountsapi.dto.PhoneRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
public class Phone extends AuditModel implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(name = "id", unique = true, updatable = false)
    private String id;

    @Column(name = "number", length = 14)
    private String number;

    @Column(name = "citycode", length = 5)
    private String cityCode;

    @Column(name = "countrycode", length = 3)
    private String countryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    /**
     * Creates a new instance of Phone
     * @param dto request object
     * @param user user entity
     * @return Phone instance
     */
    public static Phone fromRequest(PhoneRequestDto dto, User user) {
        Phone phone = new Phone();
        phone.setNumber(dto.number());
        phone.setCityCode(dto.cityCode());
        phone.setCountryCode(dto.countryCode());
        phone.setUser(user);
        return phone;
    }
}