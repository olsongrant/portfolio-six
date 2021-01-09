package com.formulafund.portfolio.web.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
	
	public MyUserDetailsService(UserService aUserService) {
		System.out.println("MyUserDetailsService::constructor invoked.");
		System.out.println("userService class: " + aUserService.getClass().getName());
		this.userService = aUserService;
	}
 
    private UserService userService;
    // 
    public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
    	
    	System.out.println("MyUserDetailsService::loadUserByUsername entered.");
 
        Optional<ApplicationUser> potentialUser = userService.findByEmailAddress(email);
        if (potentialUser.isEmpty()) {
            throw new UsernameNotFoundException(
              "No user found with username: "+ email);
        }
        ApplicationUser user = potentialUser.get();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        List<String> roleStrings = List.of("ROLE_USER");
        return  new org.springframework.security.core.userdetails.User
          (user.getEmailAddress(), 
          user.getPassword().toLowerCase(), enabled, accountNonExpired, 
          credentialsNonExpired, accountNonLocked, 
          getAuthorities(roleStrings));
    }
    
    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}