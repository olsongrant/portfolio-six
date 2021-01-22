package com.formulafund.portfolio.data.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.PasswordResetToken;

public interface PasswordResetTokenService extends CrudService<PasswordResetToken> {
	
    default PasswordResetToken findByToken(String aToken) {
		Optional<PasswordResetToken> potentialToken = this.findAll()
				.stream()
				.filter(token -> aToken.equals(token.getToken()))
				.findAny();
		return (potentialToken.isPresent() ? potentialToken.get() : null);  	
    }

    default PasswordResetToken findByUser(ApplicationUser user) {
		Optional<PasswordResetToken> potentialToken = this.findAll()
				.stream()
				.filter(token -> user.equals(token.getUser()))
				.findAny();
		return (potentialToken.isPresent() ? potentialToken.get() : null);  	    	
    }

    default Stream<PasswordResetToken> findAllByExpiryDateLessThan(LocalDateTime now) {
		Set<PasswordResetToken> tokens = this.findAll()
				.stream()
				.filter(token -> token.getExpiryDate().isBefore(now))
				.collect(Collectors.toSet());
		return tokens.stream();   	
    }

    default void deleteByExpiryDateLessThan(LocalDateTime now) {
    	this.findAllByExpiryDateLessThan(now)
    		.forEach(t -> this.delete(t));
    }

    default void deleteAllExpiredSince(LocalDateTime now) {
    	this.deleteByExpiryDateLessThan(now);
    }
    
    default boolean isTokenExpired(String aToken) {
    	PasswordResetToken tokenObject = this.findByToken(aToken);
    	LocalDateTime expirationDate = tokenObject.getExpiryDate();
    	return (expirationDate.isBefore(LocalDateTime.now())); 
    }
    
    public void createPasswordResetTokenForUser(final ApplicationUser user, final String token);
    
    public PasswordResetToken getPasswordResetToken(final String token);
}
