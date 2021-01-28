package com.formulafund.portfolio.web.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.formulafund.portfolio.data.commands.BuyCommand;
import com.formulafund.portfolio.data.commands.DeleteUserCommand;
import com.formulafund.portfolio.data.commands.RegisterUserCommand;
import com.formulafund.portfolio.data.commands.SocialUserCommand;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.FacebookUser;
import com.formulafund.portfolio.data.model.PasswordResetToken;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.VerificationToken;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.PasswordResetTokenService;
import com.formulafund.portfolio.data.services.TransactionService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.data.services.VerificationTokenService;
import com.formulafund.portfolio.web.commands.AccountCommand;
import com.formulafund.portfolio.web.commands.AddAccountCommand;
import com.formulafund.portfolio.web.commands.UserCommand;
import com.formulafund.portfolio.web.converters.UserToUserCommand;
import com.formulafund.portfolio.web.security.CustomAuthenticationProvider;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
	  
	private AccountService accountService;
	private UserService userService;
	private ApplicationEventPublisher eventPublisher;
	private VerificationTokenService verificationTokenService;
	private TransactionService transactionService;
	private PasswordResetTokenService passwordTokenService;
		
	public UserController(AccountService aService, 
						  UserService uService,
						  MessageSource mSource,
						  ApplicationEventPublisher anEventPublisher,
						  VerificationTokenService aVerificationTokenService,
						  TransactionService aTransactionService,
						  PasswordResetTokenService aPasswordTokenService) {
		this.accountService = aService;
		this.userService = uService;
		this.eventPublisher = anEventPublisher;
		this.verificationTokenService = aVerificationTokenService;
		this.transactionService = aTransactionService;
		this.passwordTokenService = aPasswordTokenService;
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
	
	@GetMapping("/user/{userid}/delete")
	public String provideDeletionForm(@PathVariable String userid, Model model, HttpServletRequest request) {
		log.info("The deletion of user with identifier " + userid + " has been requested.");
		Long idLong = Long.valueOf(userid);
		ApplicationUser user = this.userService.findById(idLong);
		if ((request.getRemoteUser() != null) && (request.getRemoteUser().equals(user.getEmailAddress()))) {
			model.addAttribute("user", user);
			DeleteUserCommand command = new DeleteUserCommand();
			command.setUserId(userid);
			command.setEmailAddress(user.getEmailAddress());
			model.addAttribute("command", command);
			return "user/delete";
		} else {
			model.addAttribute("resultmessage", "Unexpected: something went awry with the logged-in user information. "
					+ "Recommendation: log out and log back in before trying again.");
			return "simple";
		}
		
	}
	
	@PostMapping("/user/delete")
	public String deleteUserEntirely(@Valid @ModelAttribute("command") DeleteUserCommand command, 
    						   BindingResult bindingResult, HttpServletRequest request, Model model) {
    	log.info("UserController::deleteUserEntirely");
    	log.info("DeleteUserCommand: " + command);
    	if (bindingResult.hasErrors()) {
    		return "user/registration";
    	} else {
    		Long idLong = Long.valueOf(command.getUserId());
    		ApplicationUser user = this.userService.findById(idLong);
    		Set<Account> accounts = user.getAccounts();
    		log.info("User has " + accounts.size() + " accounts to be deleted.");
    		for (Account account: accounts) {
    			Set<Transaction> transactions = account.getTransactions();  			
    			log.info("Account " + account.getName() + " has " + transactions.size() + " to be deleted.");
    			for (Transaction transaction: transactions) {
    				log.info("Transaction to be deleted: " + transaction.toString());
    				this.transactionService.delete(transaction);
    			}
    			this.accountService.delete(account);
    		}
    		VerificationToken aToken = this.verificationTokenService.findByUser(user);
    		if (aToken != null) {
    			this.verificationTokenService.delete(aToken);
    		}
    		PasswordResetToken passwordReset = this.passwordTokenService.findByUser(user);
    		if (passwordReset != null) {
    			this.passwordTokenService.delete(passwordReset);
    		}
    		this.userService.delete(user);
    		return "redirect:/logout";
    	}
    	
	}
	
	@RequestMapping(value="/logout",method = RequestMethod.GET)
    public String logout(Principal principal, HttpServletRequest request){
		
		try {
			Authentication authentication = (Authentication) principal;
			log.info("logging out: " + authentication.getPrincipal().getClass().getName());
		} catch(Exception e) {
			log.info("exception caught from logout(): " + e.getMessage());
		}
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
    public String registerUser(@Valid @ModelAttribute("user") RegisterUserCommand command, 
    						   BindingResult bindingResult, HttpServletRequest request, Model model){
    	
    	log.info("UserController::registerUser");
    	log.info("RegisterUserCommand: " + command);
    	if (bindingResult.hasErrors()) {
    		return "user/registration";
    	}
    	String requestOrigin = request.getHeader("origin");
    	ApplicationUser user = this.userService.registerUser(command);
    	this.eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, 
    			request.getLocale(),
    			requestOrigin));
//    	String destination = "redirect:/user/" + user.getId() + "/show";
		model.addAttribute("hasInfo", true);
		model.addAttribute("info", "You should get an email message from us momentarily. Invoke the link in that email message to finish the registration process.");
    	String destination = "register/waiting";
        return destination;
    }
	
	@Transactional
    @PostMapping("/socialregister")
    public String registerSocialUser(@Valid @ModelAttribute("socialuser") SocialUserCommand command,
    								 BindingResult bindingResult){
    	
    	log.info("UserController::registerSocialUser");
    	log.info("SocialUserCommand: " + command);
    	if (bindingResult.hasErrors()) {
    		return "register/social";
    	}
    	ApplicationUser user = this.userService.registerSocialUser(command);
    	String destination = "redirect:/oauth2/authorize-client/facebook";
        return destination;
    }
	
    @GetMapping("/registrationConfirm")
    public String confirmRegistration(final HttpServletRequest request, Model model, @RequestParam("token") final String token) throws UnsupportedEncodingException {
        Locale locale = request.getLocale();
        model.addAttribute("lang", locale.getLanguage());
        final String result = this.verificationTokenService.validateVerificationToken(token, this.userService);
        if (result.equals("valid")) {
        	VerificationToken vToken = this.verificationTokenService.findByToken(token);
        	
            final ApplicationUser user = vToken.getUser();
            // if (user.isUsing2FA()) {
            // model.addAttribute("qr", userService.generateQRUrl(user));
            // return "redirect:/qrcode.html?lang=" + locale.getLanguage();
            // }
            Authentication authentication = 
            		CustomAuthenticationProvider.createUsernamePasswordAuthenticationToken(user.getCredentials(), user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            model.addAttribute("messageKey", "message.accountVerified");
    		log.info("registration email was confirmed");
    		model.addAttribute("hasInfo", true);
    		model.addAttribute("info", "Your email address has been verified. Your account is enabled. You are logged in.");
    		model.addAttribute("userSet", this.userService.findAll());
            return "index";
        }

        model.addAttribute("messageKey", "auth.message." + result);
        model.addAttribute("expired", "expired".equals(result));
        model.addAttribute("token", token);
   		model.addAttribute("hasInfo", true);
		model.addAttribute("info", "Your email address confirmation did not process successfully.");
        return "badUser";
    }
}
