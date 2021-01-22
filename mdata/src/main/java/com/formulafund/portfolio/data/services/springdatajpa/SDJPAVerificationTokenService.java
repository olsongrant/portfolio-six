package com.formulafund.portfolio.data.services.springdatajpa;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.PasswordResetToken;
import com.formulafund.portfolio.data.model.VerificationToken;
import com.formulafund.portfolio.data.repositories.PasswordResetTokenRepository;
import com.formulafund.portfolio.data.repositories.VerificationTokenRepository;
import com.formulafund.portfolio.data.services.PasswordResetTokenService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.data.services.VerificationTokenService;

@Service
@Profile({"mysqldev", "h2dev", "mysqlprod"})
public class SDJPAVerificationTokenService implements VerificationTokenService {
	
	private VerificationTokenRepository verificationTokenRepository;
	
    public SDJPAVerificationTokenService(
    		VerificationTokenRepository aVerificationTokenRepository) {
    	this.verificationTokenRepository = aVerificationTokenRepository;
    }
    
	@Override
	public Set<VerificationToken> findAll() {
		Set<VerificationToken> tokens = new HashSet<>();
		this.verificationTokenRepository.findAll().forEach(tokens::add);
		return tokens;
	}

	@Override
	public VerificationToken findById(Long id) {
		return this.verificationTokenRepository.findById(id).orElse(null);
	}

	@Override
	public VerificationToken save(VerificationToken object) {
		return this.verificationTokenRepository.save(object);
	}

	@Override
	public void delete(VerificationToken object) {
		this.verificationTokenRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		this.verificationTokenRepository.deleteById(id);
	}

    @Override
    public void createVerificationTokenForUser(final ApplicationUser user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        this.verificationTokenRepository.save(myToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = this.verificationTokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
            .toString());
        vToken = this.verificationTokenRepository.save(vToken);
        return vToken;
    }
	
    @Override
    public String validateVerificationToken(String token, UserService aUserService) {
        final VerificationToken verificationToken = this.verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final ApplicationUser user = verificationToken.getUser();
        final LocalDateTime now = LocalDateTime.now();
        if (verificationToken.getExpiryDate().isBefore(now)) {
            this.verificationTokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        // tokenRepository.delete(verificationToken);
        aUserService.save(user);
        return TOKEN_VALID;
    }

}
