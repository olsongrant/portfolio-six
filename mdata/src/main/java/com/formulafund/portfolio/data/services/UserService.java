package com.formulafund.portfolio.data.services;

import java.util.Optional;
import java.util.Set;

import com.formulafund.portfolio.data.commands.RegisterUserCommand;
import com.formulafund.portfolio.data.commands.SocialUserCommand;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.SocialPlatformUser;
import com.formulafund.portfolio.data.model.PasswordResetToken;


public interface UserService extends CrudService<ApplicationUser> {

	ApplicationUser registerUser(RegisterUserCommand command);



	ApplicationUser registerSocialUser(SocialUserCommand socialUser);

	public default ApplicationUser setupUser(RegisterUserCommand command, PasswordEncoderService encoder) {
		ApplicationUser user = new ApplicationUser();  // in a really unlikely scenario, this will be thrown away
		Optional<ApplicationUser> potentialExistingUser = this.findByEmailAddress(command.getEmail());
		if (potentialExistingUser.isPresent()) {
			user = potentialExistingUser.get();
		}
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
		Optional<ApplicationUser> potentialExistingUser = this.findBySocialPlatformId(command.getSocialPlatformId());
		if (potentialExistingUser.isPresent()) {
			user = potentialExistingUser.get();
		}
		user.setEmailAddress(command.getEmail());
		user.setFirstName(command.getFirstName());
		user.setLastName(command.getLastName());
		user.setHandle(command.getHandle());
		if ((command.getSocialPlatformId() != null) && (!command.getSocialPlatformId().isBlank())) {
			user.setSocialPlatformId(command.getSocialPlatformId());
		}
		user.setEnabled(true);
		user = this.save(user);
		return user;
	}

	public default Optional<ApplicationUser> findByEmailAddress(String emailAddress) {
		Set<ApplicationUser> userSet = this.findAll();
		Optional<ApplicationUser> potentialUser = userSet.stream()
				.filter(u -> emailAddress.equalsIgnoreCase(u.getEmailAddress())).findAny();
		return potentialUser;
	}
	
	public default Optional<ApplicationUser> findBySocialPlatformId(String platformId) {
		Set<ApplicationUser> userSet = this.findAll();
		Optional<ApplicationUser> potentialUser = userSet.stream()
				.filter(u -> platformId.equalsIgnoreCase(u.getSocialPlatformId())).findAny();
		return potentialUser;
	}
	
	public void changeUserPassword(final ApplicationUser user, final String password);

    public Optional<ApplicationUser> getUserByPasswordResetToken(final String token); 
}
