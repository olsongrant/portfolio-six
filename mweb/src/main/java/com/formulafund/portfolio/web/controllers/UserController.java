package com.formulafund.portfolio.web.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.formulafund.portfolio.data.commands.BuyCommand;
import com.formulafund.portfolio.data.commands.RegisterUserCommand;
import com.formulafund.portfolio.data.commands.SocialUserCommand;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.FacebookUser;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.web.commands.AccountCommand;
import com.formulafund.portfolio.web.commands.AddAccountCommand;
import com.formulafund.portfolio.web.commands.UserCommand;
import com.formulafund.portfolio.web.converters.UserToUserCommand;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
	  
	private AccountService accountService;
	private UserService userService;
		
	public UserController(AccountService aService, 
						  UserService uService,
						  MessageSource mSource) {
		this.accountService = aService;
		this.userService = uService;
	}
	
	@RequestMapping({"user", "user/index"})
	public String getIndex(Model model) {
		log.info("user index page was requested");
		model.addAttribute("accountSet", this.accountService.findAll());
		return "user/index";
	}
	
	@RequestMapping("/user/{id}/show")
	public String showAccountsForUser(@PathVariable String id, Model model, HttpServletRequest request) {
		log.info("show user " + id);
		Long idLong = Long.valueOf(id);
		ApplicationUser user = this.userService.findById(idLong);
		model.addAttribute("user", user);
		if ((request.getRemoteUser() != null) && (request.getRemoteUser().equals(user.getEmailAddress()))) {
			model.addAttribute("allowAdd", true);
		} else {
			// we want to inform the user to login so that they can add an account
		}
		Set<Account> accountSet = user.getAccounts();
		model.addAttribute("accountSet", accountSet);
		return "user/show";
	}
	
	@RequestMapping("/user/{id}/addaccount")
	public String provideAddAccountForm(@PathVariable String id, Model model) {
		log.debug("provide Add Account Form for user id " + id);
		Long idLong = Long.valueOf(id);
		ApplicationUser user = this.userService.findById(idLong);
		AddAccountCommand command = new AddAccountCommand();
		command.setId(id);
		command.setUserFullName(user.getFullName());
		command.setUserHandle(user.getHandle());
		model.addAttribute("addaccount", command);
		return "user/accountadd";
	}
	
	@RequestMapping(value="/logout",method = RequestMethod.GET)
    public String logout(Principal principal, HttpServletRequest request){
		Authentication authentication = (Authentication) principal;
//		org.springframework.security.core.userdetails.User securityCoreUser = 
//				(org.springframework.security.core.userdetails.User) authentication.getPrincipal();
		log.info(authentication.getPrincipal().getClass().getName());
//		log.info(securityCoreUser.getUsername() + " logged out");
        HttpSession httpSession = request.getSession();
        httpSession.invalidate();
        return "redirect:/";
    }
	
	@GetMapping("/user/registration")
	public String showRegistrationForm(WebRequest request, Model model) {
	    RegisterUserCommand userDto = new RegisterUserCommand();
	    model.addAttribute("user", userDto);
	    return "user/registration";
	}
	
	@Transactional
    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterUserCommand command){
    	
    	log.info("UserController::registerUser");
    	log.info("RegisterUserCommand: " + command);
    	ApplicationUser user = this.userService.registerUser(command);
    	String destination = "redirect:/user/" + user.getId() + "/show";
        return destination;
    }
	
	@Transactional
    @PostMapping("/socialregister")
    public String registerSocialUser(@ModelAttribute SocialUserCommand command){
    	
    	log.info("UserController::registerSocialUser");
    	log.info("SocialUserCommand: " + command);
    	ApplicationUser user = this.userService.registerSocialUser(command);
    	String destination = "redirect:/oauth2/authorize-client/facebook";
        return destination;
    }
}
