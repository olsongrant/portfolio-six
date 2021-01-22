package com.formulafund.portfolio.data.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.VerificationToken;

public interface VerificationTokenService extends CrudService<VerificationToken> {
	
    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";
    
    default VerificationToken findByToken(String aToken) {
    	Optional<VerificationToken> potentialToken = this.findAll()
    			.stream()
    			.filter(vToken -> aToken.equals(vToken.getToken()))
    			.findAny();
    	return (potentialToken.isPresent() ? potentialToken.get() : null);  	
    }

    default VerificationToken findByUser(ApplicationUser user) {
    	Optional<VerificationToken> potentialToken = this.findAll()
    			.stream()
    			.filter(token -> user.equals(token.getUser()))
				.findAny();
		return (potentialToken.isPresent() ? potentialToken.get() : null);  	    	
    }

    default Stream<VerificationToken> findAllByExpiryDateLessThan(LocalDateTime now) {
    	Set<VerificationToken> tokens = this.findAll()
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

    public String validateVerificationToken(String token, UserService aUserService);
    
    public void createVerificationTokenForUser(final ApplicationUser user, final String token);
    
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken);
    
}
