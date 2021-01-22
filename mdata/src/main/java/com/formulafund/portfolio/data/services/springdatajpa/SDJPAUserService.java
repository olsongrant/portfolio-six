package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.commands.RegisterUserCommand;
import com.formulafund.portfolio.data.commands.SocialUserCommand;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.PasswordResetToken;
import com.formulafund.portfolio.data.model.VerificationToken;
import com.formulafund.portfolio.data.repositories.UserRepository;
import com.formulafund.portfolio.data.services.PasswordEncoderService;
import com.formulafund.portfolio.data.services.PasswordResetTokenService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.data.services.VerificationTokenService;

@Service
@Profile({"mysqldev", "h2dev", "mysqlprod"})
public class SDJPAUserService implements UserService {
	
	private UserRepository userRepository;
	
	private PasswordEncoderService encoderService;
	
	private VerificationTokenService verificationTokenService;
	private PasswordResetTokenService passwordTokenService;
	
	public SDJPAUserService(UserRepository uRepository,
							PasswordEncoderService anEncoderService,
							VerificationTokenService aVerificationTokenService,
							PasswordResetTokenService aPasswordResetTokenService) {
		this.userRepository = uRepository;
		this.encoderService = anEncoderService;
		this.verificationTokenService = aVerificationTokenService;
		this.passwordTokenService = aPasswordResetTokenService;
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
	public void delete(ApplicationUser aUser) {
        final VerificationToken verificationToken = this.verificationTokenService.findByUser(aUser);

        if (verificationToken != null) {
            this.verificationTokenService.delete(verificationToken);
        }

        final PasswordResetToken passwordToken = this.passwordTokenService.findByUser(aUser);

        if (passwordToken != null) {
            this.passwordTokenService.delete(passwordToken);
        }	
		
		this.userRepository.delete(aUser);
	}

	@Override
	public void deleteById(Long id) {
		ApplicationUser aUser = this.findById(id);
		this.delete(aUser);
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
	
    @Override
    public void changeUserPassword(final ApplicationUser user, final String password) {
        user.setPassword(this.encoderService.encode(password));
        userRepository.save(user);
    }
    


    @Override
    public Optional<ApplicationUser> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(this.passwordTokenService.findByToken(token).getUser());
    }
	
}
