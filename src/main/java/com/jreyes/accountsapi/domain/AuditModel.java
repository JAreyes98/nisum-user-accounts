package com.jreyes.accountsapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel implements AuditableEntity, Serializable {

    @CreatedDate
    @Column(name = "createdOn", nullable = false)
    private Timestamp createdOn;

    @LastModifiedDate
    @Column(name = "updatedOn")
    private Timestamp updatedOn;
}
