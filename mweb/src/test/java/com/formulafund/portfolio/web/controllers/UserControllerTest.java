package com.formulafund.portfolio.web.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.PasswordResetTokenService;
import com.formulafund.portfolio.data.services.TransactionService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.data.services.VerificationTokenService;

class UserControllerTest {
	@Mock
	Model model;
	

	UserController controller;
	ApplicationUser kirk = ApplicationUser.with("James",  "Kirk", "kirk");
	
	@Mock
	AccountService accountService;
	
	@Mock
	UserService userService;
	
	@Mock
	MessageSource messageSource;
	
	@Mock
	ApplicationEventPublisher eventPublisher;
	
	@Mock
	VerificationTokenService verificationTokenService;
	
	@Mock
	TransactionService transactionService;
	
	@Mock
	PasswordResetTokenService passwordTokenService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		controller = new UserController(
				this.accountService, 
				this.userService,
				this.messageSource,
				this.eventPublisher,
				this.verificationTokenService,
				this.transactionService,
				this.passwordTokenService);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetIndex() {
		Account etfAccount = Account.with("ETFs All The Way", kirk);
		Account preferreds = Account.with("Preferreds are Preferred", kirk);
		Set<Account> accounts = new HashSet<>();
		accounts.add(etfAccount);
		accounts.add(preferreds);
		accountService.save(etfAccount);
		accountService.save(preferreds);
		when(accountService.findAll()).thenReturn(accounts);
		model.addAttribute("accountSet", accounts);
		String viewName = controller.getIndex(model);
		assertEquals("user/index", viewName);
	}

}
