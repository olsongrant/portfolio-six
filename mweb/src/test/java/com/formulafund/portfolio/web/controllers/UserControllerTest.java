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
import org.springframework.ui.Model;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.User;
import com.formulafund.portfolio.data.services.AccountService;

class UserControllerTest {
	@Mock
	Model model;
	

	UserController controller;
	User kirk = User.with("James T. Kirk", "kirk");
	
	@Mock
	AccountService accountService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		controller = new UserController(this.accountService);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetIndex() {
		Account etfAccount = Account.with("ETFs All The Way", kirk);
		Account preferreds = Account.with("Preferreds are Preferre", kirk);
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
