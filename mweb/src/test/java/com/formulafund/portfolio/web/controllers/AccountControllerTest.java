package com.formulafund.portfolio.web.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.TransactionService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.web.commands.AddAccountCommand;



class AccountControllerTest {
	
	@Mock
	private UserService userService;
	
	@Mock
	private AccountService accountService;
	
	@Mock
	private TransactionService transactionService;
	
	private AccountController controller;
	
	MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		controller = new AccountController(this.accountService, 
				 						   this.userService,
				 						   this.transactionService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSaveOrUpdate() throws Exception {
        //given
        AddAccountCommand command = new AddAccountCommand();
        command.setAccountName("universal");
        command.setId("12");
        command.setUserFullName("James Kirk");
        command.setUserHandle("captainkirk");
        
        ApplicationUser kirkUser = ApplicationUser.with("James", "Kirk", "captainkirk");
        kirkUser.setEmailAddress("kirk@enterprise.com");
        

        //when
        when(this.accountService.save(any())).thenReturn(new Account());
        when(this.userService.findById(anyLong())).thenReturn(kirkUser);
        
        //then
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "12")
                .param("accountName", "universal")
        )
                .andExpect(status().is3xxRedirection());
//                .andExpect(view().name("redirect:/recipe/2/ingredient/3/show"));
	}

	@Test
	void testProvideAddAccountForm() {
//		fail("Not yet implemented");
	}

	@Test
	void testRequestAccountDeleteForm() {
//		fail("Not yet implemented");
	}

	@Test
	void testDeleteAccount() {
//		fail("Not yet implemented");
	}

}
