package com.formulafund.portfolio.web.controllers;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.formulafund.portfolio.data.commands.PasswordResetCommand;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.PasswordResetToken;
import com.formulafund.portfolio.data.services.PasswordResetTokenService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.data.services.VerificationTokenService;
import com.formulafund.portfolio.web.commands.ResendVerificationEmailCommand;
import com.formulafund.portfolio.web.commands.ResetPasswordCommand;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ResendController {
	
    private JavaMailSender mailSender;
    private Environment env;   
    private VerificationTokenService verificationTokenService;
    private PasswordResetTokenService passwordTokenService;
    private UserService userService;
	
	public ResendController(JavaMailSender aMailSender,
							 Environment anEnvironment,
							 VerificationTokenService aVerificationTokenService,
							 PasswordResetTokenService aPasswordTokenService,
							 UserService aUserService) {
		this.mailSender = aMailSender;
		this.env = anEnvironment;
		this.verificationTokenService = aVerificationTokenService;
		this.passwordTokenService = aPasswordTokenService;
		this.userService = aUserService;
	}

	@GetMapping("/user/passwordreset")
	public String needsPasswordReset(Model model) {
	    ResetPasswordCommand userDto = new ResetPasswordCommand();
	    model.addAttribute("user", userDto);		
		return "user/needsreset";
	}
	
	@GetMapping("/resendverification") 
	public String needsVerificationResent(Model model) {
		ResendVerificationEmailCommand command = new ResendVerificationEmailCommand();
		model.addAttribute("user", command);
		return "user/needsverification";
	}
	
	@PostMapping("/user/verifyemail")
    public String sendVerificationEmail(@Valid @ModelAttribute("user") ResendVerificationEmailCommand command,
    						   BindingResult bindingResult, Model model, HttpServletRequest request){

    	log.info("ResendController::sendVerificationEmail");
    	log.info("ResendVerificationEmailCommand: " + command);
    	if (bindingResult.hasErrors()) {
    		return "user/needsverification";
    	}
    	String emailAddress = command.getEmail();
        Optional<ApplicationUser> potentialUser = this.userService.findByEmailAddress(emailAddress);
        if (potentialUser.isEmpty()) {
        	model.addAttribute("provideRegisterLink", Boolean.TRUE);
        	model.addAttribute("resultmessage", "No registration record for " + emailAddress +
        						". Recommendation: register.");
        	return "simple";
        }
        ApplicationUser user = potentialUser.get();
        final String token = UUID.randomUUID().toString();
        String contextPath = request.getHeader("origin");
        this.verificationTokenService.createVerificationTokenForUser(user, token);
        SimpleMailMessage outgoing = this.constructResendVerificationTokenEmail(contextPath, token, emailAddress);
        this.mailSender.send(outgoing);
        model.addAttribute("resultmessage", "Registration confirmation email sent for " + emailAddress);
        return "simple";
    }
    

    // Reset password
    @PostMapping("/user/sendreset")
    public String sendResetPasswordEmail(@Valid @ModelAttribute("user") ResetPasswordCommand command, 
    							final HttpServletRequest request, 
    							BindingResult bindingResult,
    							Model model) {
    	log.info("ResendController::sendVerificationEmail");
    	log.info("ResendVerificationEmailCommand: " + command);
    	if (bindingResult.hasErrors()) {
    		return "user/needsreset";
    	}
    	String emailAddress = command.getEmail();
        Optional<ApplicationUser> potentialUser = this.userService.findByEmailAddress(emailAddress);
        if (potentialUser.isEmpty()) {
        	model.addAttribute("provideRegisterLink", Boolean.TRUE);
        	model.addAttribute("resultmessage", "No user record for " + emailAddress +
        			 		   ". Recommendation: register.");
        	return "simple";
        }
        final ApplicationUser user = potentialUser.get();
        String contextPath = request.getHeader("origin");
 
        final String token = UUID.randomUUID().toString();
        this.passwordTokenService.createPasswordResetTokenForUser(user, token);
        mailSender.send(constructResetTokenEmail(contextPath, token, emailAddress));
        model.addAttribute("resultmessage", "Password reset message sent to " + emailAddress);
    	return "simple";
    }
    
    @GetMapping("/user/changePassword")
    public String enablePasswordReset(final HttpServletRequest request, 
    								  Model model, 
    								  @RequestParam("token") final String token) 
    										  throws UnsupportedEncodingException {
    	Optional<ApplicationUser> potentialUser = this.userService.getUserByPasswordResetToken(token);
    	if (potentialUser.isEmpty()) {
    		model.addAttribute("provideRegisterLink", Boolean.TRUE);
        	model.addAttribute("resultmessage", "No user record for the token that was supplied. Recommendation: register.");
        	return "simple";    		
    	}
    	ApplicationUser user = potentialUser.get();

    	if (this.passwordTokenService.isTokenExpired(token)) {
    		model.addAttribute("providePasswordReset", Boolean.TRUE);
    		model.addAttribute("resultmessage", "Unfortunately, the password reset token has expired. "
    				+ "Recommendation: request another password reset email message.");
    		return "simple";
    	}
    	PasswordResetCommand command = new PasswordResetCommand();
    	command.setToken(token);
    	command.setEmail(user.getEmailAddress());
//        model.addAttribute("expired", "expired".equals(result));
    	model.addAttribute("command", command);
        return "user/passwordchange";
    }

    // Save password
    @PostMapping("/user/savePassword")
    public String savePassword(@Valid @ModelAttribute("command") PasswordResetCommand passwordDto,
    							BindingResult result,
    							HttpServletRequest request, 
    							Model model) {

    	Optional<ApplicationUser> potentialUser = this.userService.getUserByPasswordResetToken(passwordDto.getToken());
    	if (potentialUser.isEmpty()) {
    		model.addAttribute("providePasswordReset", Boolean.TRUE);
    		model.addAttribute("provideRegisterLink", Boolean.TRUE);
        	model.addAttribute("resultmessage", "No user record for the token that was supplied.");
        	return "simple";    		
    	}
    	ApplicationUser user = potentialUser.get();
    	this.userService.changeUserPassword(user, passwordDto.getPassword());
    	model.addAttribute("resultmessage", "Password successfully changed.");
    	return "simple";    
    }
	
    private SimpleMailMessage constructResendVerificationTokenEmail(
    												final String contextPath, 
    												final String newToken, 
    												final String userAddress) {
        final String confirmationUrl = contextPath + "/registrationConfirm?token=" + newToken;
        final String message = "Here is your registration confirmation link. "
        		+ "Clicking on it should lead to registration confirmation. ";
        return constructEmail("Email Address Verification, Re-sent", message + " \r\n" + confirmationUrl, userAddress);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, 
    												   final String token, 
    												   final String userAddress) {
        final String url = contextPath + "/user/changePassword?token=" + token;
        final String message = "Clicking on this link should allow you re-set your password. ";
        return constructEmail("Password Reset Link", message + " \r\n" + url, userAddress);
    }

    private SimpleMailMessage constructEmail(String subject, String body, String userAddress) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(userAddress);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
