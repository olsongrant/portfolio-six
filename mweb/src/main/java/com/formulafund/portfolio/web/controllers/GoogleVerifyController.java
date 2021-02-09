package com.formulafund.portfolio.web.controllers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.formulafund.portfolio.data.commands.SocialUserCommand;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.SocialPlatformUser;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.web.converters.SocialConverter;
import com.formulafund.portfolio.web.security.CustomAuthenticationProvider;
import com.google.api.client.googleapis.apache.GoogleApacheHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class GoogleVerifyController {
	protected static final String GOOGLE_API_CLIENT_ID = 
			"775071558769-8e91vg1mqvpd7c73i5frmsjtoeitcjnj.apps.googleusercontent.com";
	
	private GoogleIdTokenVerifier tokenVerifier = GoogleVerifyController.instantiateGoogleHelper(GOOGLE_API_CLIENT_ID);
	private UserService userService;
	
	public GoogleVerifyController(UserService aUserService) {
		this.userService = aUserService;
	}
	
	@GetMapping("/googleLogin")
	public String provideGoogleLogin() {
		return "googleLogin";
	}

	@PostMapping("/verify")
	public String receiveToken(@RequestParam("idToken") String idTokenString, Model model) {
		System.out.println("posted request body: " + idTokenString);

		GoogleIdToken idToken = null;
		try {
			idToken = this.tokenVerifier.verify(idTokenString);
		} catch (GeneralSecurityException | IOException e) {
			log.warn("Thrown from token verification", e);
			model.addAttribute("previousAttemptMessage", "The Google Sign-In process failed.");
			return "login";
		} 
		StringBuilder sb = new StringBuilder("Google user info: ");
		if (idToken != null) {
			SocialPlatformUser googleUser = populateGoogleUser(idToken, sb);
			log.info(sb.toString());
			// Use or store profile information
    	    Optional<ApplicationUser> potentialUser = this.userService.findBySocialPlatformId(googleUser.getId());
    	    if (potentialUser.isEmpty()) {
    	    	SocialUserCommand command = SocialConverter.commandForSocialPlatformUser(googleUser);
    	    	this.userService.setupSocialUser(command);
    	    	model.addAttribute("socialuser", command);
    	    	return "register/google";
    	    }
    	    ApplicationUser appUser = potentialUser.get();
    	    if (appUser.getHandle() == null) {
    	    	SocialUserCommand command = SocialConverter.commandForSocialPlatformUser(googleUser);
    	    	this.userService.setupSocialUser(command);
    	    	model.addAttribute("socialuser", command);
    	    	return "register/google";
    	    } else {
    	        Authentication authentication = 
    	        		CustomAuthenticationProvider.createUsernamePasswordAuthenticationToken(
    	        				appUser.getCredentials(), appUser);
    	        SecurityContextHolder.getContext().setAuthentication(authentication);
    			log.info("google user is logged in successfully");
    			model.addAttribute("hasInfo", true);
    			model.addAttribute("info", "You are logged in.");
    			model.addAttribute("userSet", this.userService.findAll());
    	        return "index";
    	    }
		} else {
		  log.warn("Invalid ID token.");
		}
		model.addAttribute("previousAttemptMessage", "The Google Sign-In process failed.");
		return "login";
	}







	public SocialPlatformUser populateGoogleUser(GoogleIdToken idToken, StringBuilder sb) {
		SocialPlatformUser googleUser = new SocialPlatformUser();
		Payload payload = idToken.getPayload();

		// Print user identifier
		String userId = payload.getSubject();
		System.out.println("User ID: " + userId);
		sb.append("User ID: " + userId);
		sb.append(System.lineSeparator());
		googleUser.setId(userId);
		// Get profile information from payload
		String email = payload.getEmail();
		sb.append("Email address: " + email);
		sb.append(System.lineSeparator());
		googleUser.setEmail(email);
		boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
		String name = (String) payload.get("name");
		String pictureUrl = (String) payload.get("picture");
		String locale = (String) payload.get("locale");
		String familyName = (String) payload.get("family_name");
		sb.append("Family name: " + familyName);
		sb.append(System.lineSeparator());
		googleUser.setLast_name(familyName);
		String givenName = (String) payload.get("given_name");
		sb.append("Given name: " + givenName);
		googleUser.setFirst_name(givenName);
		return googleUser;
	}
	

	

		
		
	
	private static GoogleIdTokenVerifier instantiateGoogleHelper(String appId) {
	    if (appId == null || appId.isEmpty()) return null;
	    return new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(), new JacksonFactory())
	            .setAudience(Collections.singletonList(appId))
	            .build();
	}	
}
