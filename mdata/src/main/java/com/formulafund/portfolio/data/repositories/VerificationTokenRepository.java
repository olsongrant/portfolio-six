package com.formulafund.portfolio.data.repositories;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.VerificationToken;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {
	
    VerificationToken findByToken(String token);

    VerificationToken findByUser(ApplicationUser user);

    Stream<VerificationToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
