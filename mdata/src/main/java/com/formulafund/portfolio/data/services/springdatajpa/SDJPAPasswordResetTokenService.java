package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.PasswordResetToken;
import com.formulafund.portfolio.data.repositories.PasswordResetTokenRepository;
import com.formulafund.portfolio.data.services.PasswordResetTokenService;

@Service
@Profile({"mysqldev", "h2dev", "mysqlprod"})
public class SDJPAPasswordResetTokenService implements PasswordResetTokenService {
	
	private PasswordResetTokenRepository passwordTokenRepository;
	
    public SDJPAPasswordResetTokenService(PasswordResetTokenRepository aPasswordResetTokenRepository) {
    	this.passwordTokenRepository = aPasswordResetTokenRepository;
    }
    
	@Override
	public Set<PasswordResetToken> findAll() {
		Set<PasswordResetToken> tokens = new HashSet<>();
		this.passwordTokenRepository.findAll().forEach(tokens::add);
		return tokens;
	}

	@Override
	public PasswordResetToken findById(Long id) {
		return this.passwordTokenRepository.findById(id).orElse(null);
	}

	@Override
	public PasswordResetToken save(PasswordResetToken object) {
		return this.passwordTokenRepository.save(object);
	}

	@Override
	public void delete(PasswordResetToken object) {
		this.passwordTokenRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		this.passwordTokenRepository.deleteById(id);
	}

    @Override
    public void createPasswordResetTokenForUser(final ApplicationUser user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        this.passwordTokenRepository.save(myToken);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return this.passwordTokenRepository.findByToken(token);
    }
}
