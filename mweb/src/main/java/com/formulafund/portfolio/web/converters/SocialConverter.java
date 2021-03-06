package com.formulafund.portfolio.web.converters;

import com.formulafund.portfolio.data.commands.SocialUserCommand;
import com.formulafund.portfolio.data.model.SocialPlatformUser;

public class SocialConverter {
	
	public static SocialUserCommand commandForSocialPlatformUser(SocialPlatformUser fbUser) {
		SocialUserCommand cmd = new SocialUserCommand();
		cmd.setEmail(fbUser.getEmail());
		cmd.setFirstName(fbUser.getFirst_name());
		cmd.setLastName(fbUser.getLast_name());
		cmd.setSocialPlatformId(fbUser.getId());
		return cmd;
	}

}
