package com.formulafund.portfolio.data.services;

import java.util.Date;
import java.util.stream.Stream;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.PasswordResetToken;

public interface PasswordResetTokenService extends CrudService<PasswordResetToken> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(ApplicationUser user);

    Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    void deleteAllExpiredSince(Date now);
}
