package com.jreyes.accountsapi.domain;

import java.sql.Timestamp;

public interface AuditableEntity {
    void setCreatedOn(Timestamp username);
    Timestamp getCreatedOn();
    void setUpdatedOn(Timestamp username);
    Timestamp getUpdatedOn();
}
