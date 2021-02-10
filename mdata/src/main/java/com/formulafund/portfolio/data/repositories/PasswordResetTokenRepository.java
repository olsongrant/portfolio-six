package com.formulafund.portfolio.data.repositories;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
	
    List<PasswordResetToken> findByToken(String token);

    List<PasswordResetToken> findByUser(ApplicationUser user);

    List<PasswordResetToken> findAllByExpiryDateLessThan(LocalDateTime now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);

}
