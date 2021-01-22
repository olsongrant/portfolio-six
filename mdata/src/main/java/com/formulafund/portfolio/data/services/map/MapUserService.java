package com.formulafund.portfolio.data.services.map;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.commands.RegisterUserCommand;
import com.formulafund.portfolio.data.commands.SocialUserCommand;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.PasswordEncoderService;
import com.formulafund.portfolio.data.services.PasswordResetTokenService;
import com.formulafund.portfolio.data.services.UserService;

@Service
@Profile("map")
public class MapUserService extends BaseMapService<ApplicationUser> implements UserService {
	
	private PasswordEncoderService encoderService;
	private PasswordResetTokenService passwordTokenService;
	
	public MapUserService(PasswordEncoderService aPasswordEncoderService,
						  PasswordResetTokenService aPasswordResetTokenService) {
		this.encoderService = aPasswordEncoderService;
		this.passwordTokenService = aPasswordResetTokenService;
	}

	@Override
	public ApplicationUser registerUser(RegisterUserCommand command) {
		ApplicationUser aUser = this.setupUser(command, this.encoderService);
		aUser = this.save(aUser);
		return aUser;
	}

	@Override
	public ApplicationUser registerSocialUser(SocialUserCommand socialUser) {
		ApplicationUser aUser = this.setupSocialUser(socialUser);
		aUser = this.save(aUser);
		return aUser;
	}

	@Override
	public void changeUserPassword(ApplicationUser user, String password) {
        user.setPassword(this.encoderService.encode(password));
        this.save(user);
	}

	@Override
	public Optional<ApplicationUser> getUserByPasswordResetToken(String token) {
		return Optional.ofNullable(this.passwordTokenService.findByToken(token) .getUser());
	}




}
