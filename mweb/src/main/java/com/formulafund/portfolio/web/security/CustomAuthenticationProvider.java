package com.formulafund.portfolio.web.security;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.PasswordEncoderService;
import com.formulafund.portfolio.data.services.UserService;

@Component
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
	    System.out.println("CustomAuthenticationProvider::authenticate. Credentials: " + name + ": " + rawPassword);
	    if (name == null) return null;
	    if (rawPassword == null) return null;
	    Optional<ApplicationUser> potentialUser = this.userService.findByEmailAddress(name);
	    if (potentialUser.isEmpty()) {
	    	return null;
	    }
	    ApplicationUser user = potentialUser.get();
	    String encodedPassword = user.getPassword();
	    boolean matches = this.encoderService.matches(rawPassword, encodedPassword);
    	Object credentials = authentication.getCredentials();
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
	    if (matches) {
	    	System.out.println("voting that the principal is authenticated from CustomAuthenticationProvider::authenticate");
	    } else {
	    	System.out.println("Passwords did not match --> voting isAuthenticated==false");
	    	votingAuth.setAuthenticated(false);
	    	return null;  // can't seem to have this do-not-authenticate bit "take", so returning null
	    }
		return votingAuth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		System.out.println("CustomAuthenticationProvider::supports invoked");
		boolean supports = authentication.equals(UsernamePasswordAuthenticationToken.class);
		System.out.println("returning from supports() method: " + supports);
		return supports;
	}

}
