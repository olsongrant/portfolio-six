package com.formulafund.portfolio.data.repositories;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
	
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(ApplicationUser user);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);

}
