package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.commands.RegisterUserCommand;
import com.formulafund.portfolio.data.commands.SocialUserCommand;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.repositories.UserRepository;
import com.formulafund.portfolio.data.services.PasswordEncoderService;
import com.formulafund.portfolio.data.services.UserService;

@Service
@Profile({"mysqldev", "h2dev", "mysqlprod"})
public class SDJPAUserService implements UserService {
	
	private UserRepository userRepository;
	
	private PasswordEncoderService encoderService;
	
	public SDJPAUserService(UserRepository uRepository,
							PasswordEncoderService anEncoderService) {
		this.userRepository = uRepository;
		this.encoderService = anEncoderService;
	}

	@Override
	public Set<ApplicationUser> findAll() {
		return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
				.collect(Collectors.toSet());
	}

	@Override
	public ApplicationUser findById(Long id) {
		return this.userRepository.findById(id).orElse(null);
	}

	@Override
	public ApplicationUser save(ApplicationUser object) {
		return this.userRepository.save(object);
	}

	@Override
	public void delete(ApplicationUser object) {
		this.userRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		this.userRepository.deleteById(id);
	}

	@Override
	public ApplicationUser registerUser(RegisterUserCommand command) {
		ApplicationUser aUser = this.setupUser(command, this.encoderService);
		return this.userRepository.save(aUser);
	}

	@Override
	public ApplicationUser registerSocialUser(SocialUserCommand socialUser) {
		ApplicationUser aUser = this.setupSocialUser(socialUser);
		aUser = this.save(aUser);
		return aUser;
	}
}
