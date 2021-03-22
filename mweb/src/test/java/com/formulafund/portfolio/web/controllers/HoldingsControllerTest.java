package com.formulafund.portfolio.web.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.PriceService;
import com.formulafund.portfolio.data.services.TickerService;



class HoldingsControllerTest {
	
	@Mock
	private AccountService accountService;
	
	@Mock
	private TickerService tickerService;
	
	@Mock
	private PriceService priceService;
	
    HoldingsController controller;

    MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		controller = new HoldingsController(this.accountService, 
											this.tickerService, 
											this.priceService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testShowAccountsForUser() throws Exception {
		final String testEmailAddress = "kirk@enterprise.com";
		Account account = new Account();
		ApplicationUser user = ApplicationUser.with("James", "Kirk", "captain");
		user.setEmailAddress(testEmailAddress);
		account.setUser(user);
		account.setCurrentCash(100000.0f);
	    when(accountService.findById(anyLong())).thenReturn(account);
	    
        this.mockMvc.perform(get("/holdings/1/show").with(
        		request -> {request.setRemoteUser(testEmailAddress);
        					return request;}))
        	.andExpect(status().isOk())
        	.andExpect(model().attributeExists("allowAdd"));  
	}

	@Test
	void testShowAccountsForUserNotMatching() throws Exception {
		final String testEmailAddress = "kirk@enterprise.com";
		Account account = new Account();
		ApplicationUser user = ApplicationUser.with("James", "Kirk", "captain");
		user.setEmailAddress(testEmailAddress);
		account.setUser(user);
		account.setCurrentCash(100000.0f);
	    when(accountService.findById(anyLong())).thenReturn(account);
	    
        this.mockMvc.perform(get("/holdings/1/show").with(
        		request -> {request.setRemoteUser("wrongaddress@aol.com");
        					return request;}))
        	.andExpect(status().isOk())
        	.andExpect( model().attributeDoesNotExist("allowAdd"));   
	}
}
