package com.formulafund.portfolio.data.services.map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.PasswordResetToken;
import com.formulafund.portfolio.data.services.PasswordResetTokenService;

@Service
@Profile("map")
public class MapPasswordResetTokenService extends BaseMapService<PasswordResetToken> implements PasswordResetTokenService {

	@Override
	public void createPasswordResetTokenForUser(ApplicationUser user, String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        this.save(myToken);
		
	}

	@Override
	public PasswordResetToken getPasswordResetToken(String token) {
		return this.findByToken(token);
	}

}
