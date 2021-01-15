package com.formulafund.portfolio.data.services;

import java.util.Optional;
import java.util.Set;

import com.formulafund.portfolio.data.commands.RegisterUserCommand;
import com.formulafund.portfolio.data.commands.SocialUserCommand;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.FacebookUser;

public interface UserService extends CrudService<ApplicationUser> {

	ApplicationUser registerUser(RegisterUserCommand command);

	ApplicationUser registerSocialUser(SocialUserCommand socialUser);

	public default ApplicationUser setupUser(RegisterUserCommand command, PasswordEncoderService encoder) {
		ApplicationUser user = new ApplicationUser();
		user.setEmailAddress(command.getEmail());
		user.setFirstName(command.getFirstName());
		user.setLastName(command.getLastName());
		user.setHandle(command.getHandle());
		String encodedPassword = encoder.encode(command.getPassword());
		user.setPassword(encodedPassword);
		user = this.save(user);
		return user;
	}

	public default ApplicationUser setupSocialUser(SocialUserCommand command) {
		ApplicationUser user = new ApplicationUser();
		user.setEmailAddress(command.getEmail());
		user.setFirstName(command.getFirstName());
		user.setLastName(command.getLastName());
		user.setHandle(command.getHandle());
		user.setSocialPlatformId(command.getSocialPlatformId());
		user = this.save(user);
		return user;
	}

	public default Optional<ApplicationUser> findByEmailAddress(String emailAddress) {
		Set<ApplicationUser> userSet = this.findAll();
		Optional<ApplicationUser> potentialUser = userSet.stream()
				.filter(u -> emailAddress.equalsIgnoreCase(u.getEmailAddress())).findAny();
		return potentialUser;
	}

}
