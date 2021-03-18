package com.formulafund.portfolio.web.controllers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formulafund.portfolio.data.commands.SocialUserCommand;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.SocialPlatformUser;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.web.converters.SocialConverter;
import com.formulafund.portfolio.web.security.CustomAuthenticationProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {
	private UserService userService;
    private ClientRegistrationRepository clientRegistrationRepository;
    private OAuth2AuthorizedClientService authorizedClientService;
    private AccountService accountService;
	protected static final String GOOGLE_API_CLIENT_ID = 
			"775071558769-8e91vg1mqvpd7c73i5frmsjtoeitcjnj.apps.googleusercontent.com";
	
	private GoogleIdTokenVerifier tokenVerifier = GoogleVerifyController.instantiateGoogleHelper(GOOGLE_API_CLIENT_ID);
    
    private static final String authorizationRequestBaseUri = "oauth2/authorize-client";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
	
	public IndexController(UserService aService,
							ClientRegistrationRepository aClientRegistrationRepository,
							OAuth2AuthorizedClientService anAuthorizedClientService,
							AccountService anAccountService) {
		this.userService = aService;
		this.clientRegistrationRepository = aClientRegistrationRepository;
		this.authorizedClientService = anAuthorizedClientService;
		this.accountService = anAccountService;
	}
	
	@RequestMapping({"", "/", "index", "index.html"})
	public String getIndex(Model model, HttpServletRequest request) {
		log.info("root index page was requested");
		String username = request.getRemoteUser();
		ApplicationUser user;
		if (username == null) {
			username = "sample@address.com";
			Optional<ApplicationUser> potentialUser = this.userService.findByEmailAddress(username);
			if (potentialUser.isEmpty()) {
				return this.provideUserListResponse(model);
			}
			user = potentialUser.get();
			Set<Account> accounts = user.getAccounts();
			if (accounts.size() > 0) {
				Account account = accounts.iterator().next();			
				return "redirect:/holdings/" + account.getId() + "/show";
			} else {
				return this.provideUserListResponse(model);
			}	
		} else {
			Optional<ApplicationUser> potentialUser = this.userService.findByEmailAddress(username);
			if (potentialUser.isEmpty()) {
				return this.provideUserListResponse(model); 
			}
			user = potentialUser.get();
			Set<Account> accounts = user.getAccounts();
			if (accounts.size() > 0) {
				Account account = accounts.iterator().next();			
				return "redirect:/holdings/" + account.getId() + "/show";
			} else {
				Long userIdLong = user.getId();
				return "redirect:/user/" + userIdLong + "/show";
			}	
		}

	}

	
	@GetMapping("/user/all")
	public String provideUserListResponse(Model model) {
		model.addAttribute("userSet", this.userService.findAll());
		return "index";
	}
	
	@GetMapping("/about")
	public String provideAboutPage() {
		return "about";
	}
	
	@GetMapping("/privacy")
	public String providePrivacyStatement() {
		return "privacy";
	}
	
	@GetMapping("/functionality")
	public String provideFunctionalityDescription() {
		return "functionality";
	}
	
	@GetMapping("/structure")
	public String provideStructureDoc() {
		return "structure";
	}
	
	@RequestMapping({"/login", "login"})
	public String getLogin(Model model, HttpServletRequest request) {
		AuthenticationException authEx = (AuthenticationException) request.getSession()
										.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (authEx != null) {
			String exceptionString = authEx.getMessage();
	        model.addAttribute("previousAttemptMessage", exceptionString);
	        String exceptionClassName = authEx.getClass().getName();
	        log.info("AuthenticationException class in getLogin(): " + exceptionClassName);
	        switch (exceptionClassName) {
	        case "org.springframework.security.authentication.BadCredentialsException":
	        case "org.springframework.security.authentication.CredentialsExpiredException":
	        	model.addAttribute("providePasswordReset", Boolean.TRUE);
	        	break;
	        case "org.springframework.security.authentication.DisabledException":
	        	model.addAttribute("provideResendOption", Boolean.TRUE);
	        	break;
	        default:
	        }
		}
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
            .as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));

        model.addAttribute("urls", oauth2AuthenticationUrls);
		return "login";
	}
	
    @GetMapping("/loginSuccess")
    public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication) {
    	OAuth2User principal = authentication.getPrincipal();
    	log.info("principal: " + principal.getAttributes());
    	log.info("credentials: " + authentication.getCredentials().toString());
    	log.info("authorized client registration id: " + authentication.getAuthorizedClientRegistrationId());
    	log.info("OAuth2AuthenticationToken::getName(): " + authentication.getName());
    	log.info("OAuth2AuthenticationToken::getDetails(): " + authentication.getDetails());
    	
    	SocialPlatformUser socialUser = this.populateGoogleUser(authentication);
    	if ((socialUser.getId() != null) && (!socialUser.getId().isEmpty())) {
    	    Optional<ApplicationUser> potentialUser = this.userService.findBySocialPlatformId(socialUser.getId());
    	    if (potentialUser.isEmpty()) {
    	    	SocialUserCommand command = SocialConverter.commandForSocialPlatformUser(socialUser);
    	    	this.userService.setupSocialUser(command);
    	    	model.addAttribute("socialuser", command);
    	    	return "register/google";
    	    }
    	    ApplicationUser appUser = potentialUser.get();
    	    if (appUser.getHandle() == null) {
    	    	SocialUserCommand command = SocialConverter.commandForSocialPlatformUser(socialUser);
    	    	this.userService.setupSocialUser(command);
    	    	model.addAttribute("socialuser", command);
    	    	return "register/google";
    	    } else {
    	        Authentication userPassAuth = 
    	        		CustomAuthenticationProvider.createUsernamePasswordAuthenticationToken(
    	        				appUser.getCredentials(), appUser);
    	        SecurityContextHolder.getContext().setAuthentication(userPassAuth);
    			log.info("google user is logged in successfully");
    			model.addAttribute("hasInfo", true);
    			model.addAttribute("info", "You are logged in.");
    			model.addAttribute("userSet", this.userService.findAll());
    	        return "index";
    	    }
    	} else {
			model.addAttribute("previousAttemptMessage", "The Google Sign-In process failed.");
			return "login";        	
        }

    }
    
    protected SocialPlatformUser populateGoogleUser(OAuth2AuthenticationToken authToken) {
    	SocialPlatformUser googleUser = new SocialPlatformUser();
    	OAuth2User principal = authToken.getPrincipal();
    	Map<String, Object> attributes = principal.getAttributes();
    	googleUser.setEmail((String) attributes.get("email"));
    	googleUser.setFirst_name((String) attributes.get("given_name"));
    	googleUser.setLast_name((String) attributes.get("family_name"));
    	googleUser.setId((String) attributes.get("sub"));
    	return googleUser;
    }
    
    @GetMapping("/loginFailure")
    public String serveLoginFailure() {
    	log.info("Login failure method was called.");
    	return "loginFailure";
    }
    
	public SocialPlatformUser getFacebookUser(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		String fields = "email,id,first_name,last_name";
		String jsonResponse = restTemplate.getForObject("https://graph.facebook.com/me/?fields=" +  fields + "&access_token=" + accessToken, String.class);
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //read json file and convert to customer object
        try {
			SocialPlatformUser fbUser = objectMapper.readValue(jsonResponse, SocialPlatformUser.class);
			log.info("fbUser: " + fbUser);
			return fbUser;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
}
