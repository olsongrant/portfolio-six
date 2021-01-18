package com.formulafund.portfolio.data.services;

import java.util.Date;
import java.util.stream.Stream;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.VerificationToken;

public interface VerificationTokenService extends CrudService<VerificationToken> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(ApplicationUser user);

    Stream<VerificationToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);
    
    void deleteAllExpiredSince(Date now);

}
