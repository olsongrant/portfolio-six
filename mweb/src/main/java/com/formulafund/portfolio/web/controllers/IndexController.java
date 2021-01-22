package com.formulafund.portfolio.web.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formulafund.portfolio.data.commands.SocialUserCommand;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.FacebookUser;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.web.converters.SocialConverter;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {
	private UserService userService;
    private ClientRegistrationRepository clientRegistrationRepository;
    private OAuth2AuthorizedClientService authorizedClientService;
    
    private static final String authorizationRequestBaseUri = "oauth2/authorize-client";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
	
	public IndexController(UserService aService,
							ClientRegistrationRepository aClientRegistrationRepository,
							OAuth2AuthorizedClientService anAuthorizedClientService) {
		this.userService = aService;
		this.clientRegistrationRepository = aClientRegistrationRepository;
		this.authorizedClientService = anAuthorizedClientService;
	}
	
	@RequestMapping({"", "/", "index", "index.html"})
	public String getIndex(Model model) {
		log.info("root index page was requested");
		model.addAttribute("userSet", this.userService.findAll());
		return "index";
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

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

        if (client != null) {
        	String accessToken = client.getAccessToken().getTokenValue();
        	FacebookUser fbUser = this.getFacebookUser(accessToken);
        	if (fbUser == null) return "loginFailure";
    	    Optional<ApplicationUser> potentialUser = this.userService.findByEmailAddress(fbUser.getEmail());
    	    if (potentialUser.isEmpty()) {
    	    	SocialUserCommand command = SocialConverter.commandForFacebookUser(fbUser);
    	    	model.addAttribute("socialuser", command);
    	    	return "register/social";
    	    }
    	    ApplicationUser user = potentialUser.get();
        	Object credentials = authentication.getCredentials();
            boolean enabled = true;
            boolean accountNonExpired = true;
            boolean credentialsNonExpired = true;
            boolean accountNonLocked = true;
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            String pwd = user.getPassword();
            if (pwd != null) {
            	pwd = pwd.toLowerCase();
            } else {
            	pwd = "socialuser";
            }
            org.springframework.security.core.userdetails.User secCoreUser = 
            		new org.springframework.security.core.userdetails.User
            				(user.getEmailAddress(), 
            					pwd, 
            					enabled, 
            					accountNonExpired, 
            					credentialsNonExpired, 
            					accountNonLocked, 
            					authorities);

        	
        	Authentication votingAuth = 
        			new UsernamePasswordAuthenticationToken(secCoreUser, 
        													credentials, 
        													authorities);
            SecurityContextHolder.getContext().setAuthentication(votingAuth);
            model.addAttribute("facebookUser", fbUser);
            String destination = "redirect:/user/" + user.getId() + "/show";
            return destination;
        }

        return "loginSuccess";
    }
    
    @GetMapping("/loginFailure")
    public String serveLoginFailure() {
    	log.info("Login failure method was called.");
    	return "loginFailure";
    }
    
	public FacebookUser getFacebookUser(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		String fields = "email,id,first_name,last_name";
		String jsonResponse = restTemplate.getForObject("https://graph.facebook.com/me/?fields=" +  fields + "&access_token=" + accessToken, String.class);
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //read json file and convert to customer object
        try {
			FacebookUser fbUser = objectMapper.readValue(jsonResponse, FacebookUser.class);
			log.info("fbUser: " + fbUser);
			return fbUser;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
}
