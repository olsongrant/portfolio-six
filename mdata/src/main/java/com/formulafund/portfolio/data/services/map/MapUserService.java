package com.formulafund.portfolio.data.services.map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.commands.RegisterUserCommand;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.PasswordEncoderService;
import com.formulafund.portfolio.data.services.UserService;

@Service
@Profile("map")
public class MapUserService extends BaseMapService<ApplicationUser> implements UserService {
	
	private PasswordEncoderService encoderService;
	
	public MapUserService(PasswordEncoderService aPasswordEncoderService) {
		this.encoderService = aPasswordEncoderService;
	}

	@Override
	public ApplicationUser registerUser(RegisterUserCommand command) {
		ApplicationUser aUser = this.setupUser(command, this.encoderService);
		aUser = this.save(aUser);
		return aUser;
	}




}
