package com.formulafund.portfolio.data.services.map;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.VerificationToken;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.data.services.VerificationTokenService;

@Service
@Profile("map")
public class MapVerificationTokenService extends BaseMapService<VerificationToken> implements VerificationTokenService {


	
	public MapVerificationTokenService() {
		super();
	}
	
	@Override
	public String validateVerificationToken(String token, UserService aUserService) {
        final VerificationToken verificationToken = this.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final ApplicationUser user = verificationToken.getUser();
        final LocalDateTime now = LocalDateTime.now();
        if (verificationToken.getExpiryDate().isBefore(now)) {
            this.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        // tokenRepository.delete(verificationToken);
        aUserService.save(user);
        return TOKEN_VALID;
	}

	@Override
	public void createVerificationTokenForUser(ApplicationUser user, String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        this.save(myToken);	
	}

	@Override
	public VerificationToken generateNewVerificationToken(String existingVerificationToken) {
        VerificationToken vToken = this.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
            .toString());
        vToken = this.save(vToken);
        return vToken;
	}

}
