package com.formulafund.portfolio.web.security;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.PasswordEncoderService;
import com.formulafund.portfolio.data.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	private UserService userService;
	private PasswordEncoderService encoderService;
	
	public CustomAuthenticationProvider(UserService aUserService,
										PasswordEncoderService passwordEncoderService) {
		this.userService = aUserService;
		this.encoderService = passwordEncoderService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	    String name = authentication.getName();
	    if (authentication.getCredentials() == null) return null;
	    String rawPassword = authentication.getCredentials().toString();
	    log.info("CustomAuthenticationProvider::authenticate. Credentials: " + name + ": " + rawPassword);
	    if (name == null) return null;
	    if (rawPassword == null) return null;
	    Optional<ApplicationUser> potentialUser = this.userService.findByEmailAddress(name);
	    if (potentialUser.isEmpty()) {
	    	log.info("Username " + name + " not found in the database");
	    	throw new UsernameNotFoundException("User for name " + name +
	    			" was not found in the system at all. Recommendation: register, whether by email address"
	    			+ " and password, or by your social media account.");
	    }
	    ApplicationUser user = potentialUser.get();
	    if (!user.isEnabled()) {
	    	log.info("User was not enabled. Throwing DisabledException!");
	    	throw new DisabledException("User for name " + name + 
	    			" is entered in the system, but the account is not marked as enabled. "
	    			+ "Most likely, the email address confirmation step has not been completed. "
	    			+ "Look for an email message from us and click on the contained link to get "
	    			+ "the account enabled.");
	    }
	    
	    String encodedPassword = user.getPassword();
	    if (encodedPassword == null) {
	    	if (user.getSocialPlatformId() != null) {
	    		log.info("User " + name + " seems to be a social platform user.");
	    		throw new WrongAuthenticationMechanismException("It seems that we have a social platform "
	    				+ "user configured in our system for " + name + ". Recommendation: log in using "
	    						+ "Facebook.");
	    	} else {
	    		log.info("User " + name + " does not have a password in the database.");
	    		throw new CredentialsExpiredException("Somehow, the password in the database does not exist. "
	    				+ "Recommendation: use the Password Reset function.");
	    	}
	    }
	    boolean matches = this.encoderService.matches(rawPassword, encodedPassword);
    	Authentication votingAuth = createUsernamePasswordAuthenticationToken(authentication, user);
	    if (matches) {
	    	log.info("voting that the principal is authenticated from CustomAuthenticationProvider::authenticate");
	    } else {
	    	log.info("Passwords did not match --> voting isAuthenticated==false");
	    	throw new BadCredentialsException("Password did not match. Recommendation: try again or "
	    			+ "use the Password Reset function.");
//	    	votingAuth.setAuthenticated(false);
//	    	return null;  // can't seem to have this do-not-authenticate bit "take", so returning null
	    }
		return votingAuth;
	}

	public static Authentication createUsernamePasswordAuthenticationToken(Object credentials,
			ApplicationUser user) {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        org.springframework.security.core.userdetails.User secCoreUser = 
        		new org.springframework.security.core.userdetails.User
        				(user.getEmailAddress(), 
        					user.getPassword().toLowerCase(), 
        					enabled, 
        					accountNonExpired, 
        					credentialsNonExpired, 
        					accountNonLocked, 
        					authorities);

    	
    	Authentication votingAuth = 
    			new UsernamePasswordAuthenticationToken(secCoreUser, 
    													credentials, 
    													authorities);
		return votingAuth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		log.info("CustomAuthenticationProvider::supports invoked");
		boolean supports = authentication.equals(UsernamePasswordAuthenticationToken.class);
		log.info("returning from supports() method: " + supports);
		return supports;
	}

}
