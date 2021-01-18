package com.formulafund.portfolio.data.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.VerificationToken;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
