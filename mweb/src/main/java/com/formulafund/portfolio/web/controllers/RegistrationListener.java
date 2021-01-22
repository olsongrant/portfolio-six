package com.formulafund.portfolio.web.controllers;

import java.util.UUID;


import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.data.services.VerificationTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private UserService userService;

    private MessageSource messages;

    private JavaMailSender mailSender;

    private Environment env;
    
    private VerificationTokenService verificationTokenService;
    
    public RegistrationListener(UserService aUserService,
    							MessageSource aMessageSource,
    							JavaMailSender aMailSender,
    							Environment anEnvironment,
    							VerificationTokenService aVerificationTokenService) {
    	this.userService = aUserService;
    	this.messages = aMessageSource;
    	this.mailSender = aMailSender;
    	this.env = anEnvironment;
    	this.verificationTokenService = aVerificationTokenService;
    }

    // API

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final ApplicationUser user = event.getUser();
        final String token = UUID.randomUUID().toString();
        this.verificationTokenService.createVerificationTokenForUser(user, token);

        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }

    //

    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, 
    												final ApplicationUser user, 
    												final String token) {
        final String recipientAddress = user.getEmailAddress();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        final String message = messages.getMessage("message.regSuccLink", null, "You registered successfully. To confirm your registration, please click on the below link.", event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
    
    public void sendTestEmail() {
    	String recipientAddress = "grant.olson@gmail.com";
    	String subject = "Test Email Message";
    	String contents = "Hello, I am a test message attempting to prove that sending messages is possible.";
    	SimpleMailMessage mailMessage = new SimpleMailMessage();
    	mailMessage.setTo(recipientAddress);
    	mailMessage.setSubject(subject);
    	mailMessage.setText(contents);
    	mailMessage.setFrom("formulafundmail@gmail.com");
    	this.mailSender.send(mailMessage);
    }
}
