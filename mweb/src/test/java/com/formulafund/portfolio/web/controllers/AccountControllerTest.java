package com.formulafund.portfolio.web.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
	
	private AddAccountCommand addAccountCommand = AccountControllerTest.makeAddAccountCommand();
	
	

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		controller = new AccountController(this.accountService, 
				 						   this.userService,
				 						   this.transactionService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	private static AddAccountCommand makeAddAccountCommand() {
        AddAccountCommand command = new AddAccountCommand();
        command.setAccountName("universal");
        command.setId("12");
        command.setUserFullName("James Kirk");
        command.setUserHandle("captainkirk");
		return command;
	}

	private static ApplicationUser makeCaptainKirkUser() {
        ApplicationUser kirkUser = ApplicationUser.with("James", "Kirk", "captainkirk");
        kirkUser.setEmailAddress("kirk@enterprise.com");
        kirkUser.setId(2L);
        return kirkUser;
	}
	
	private static Account makeCaptainKirkUniversalAccount() {
		Account account = Account.with("universal", AccountControllerTest.makeCaptainKirkUser());
		account.setId(99L);
		return account;
	}
	
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSaveOrUpdate() throws Exception {
        //given      

        //when
        when(this.accountService.save(any())).thenReturn(new Account());
        when(this.userService.findById(anyLong())).thenReturn(AccountControllerTest.makeCaptainKirkUser());
        
        //then
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "12")
                .param("accountName", "universal")
        )
                .andExpect(status().is3xxRedirection());
	}

	@Test
	void testProvideAddAccountForm() throws Exception {
        when(this.userService.findById(anyLong())).thenReturn(AccountControllerTest.makeCaptainKirkUser());
		
		mockMvc.perform(get("/user/2/addaccount"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("user/accountadd"))
			   .andExpect(model().attributeExists("addaccount"));
	}

	@Test
	void testRequestAccountDeleteForm() throws Exception {
        when(this.userService.findById(anyLong())).thenReturn(AccountControllerTest.makeCaptainKirkUser());
        when(this.accountService.findById(anyLong())).thenReturn(AccountControllerTest.makeCaptainKirkUniversalAccount());
		
		mockMvc.perform(get("/account/2/delete"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("account/delete"))
			   .andExpect(model().attributeExists("command"));
	}

	@Test
	void testDeleteAccount() throws Exception {
        when(this.accountService.findById(anyLong())).thenReturn(AccountControllerTest.makeCaptainKirkUniversalAccount());
        mockMvc.perform(post("/account/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("accountId", "12")
                .param("accountName", "universal")
                .param("acknowledged", "true")
                .param("userId", "2")
        )
                .andExpect(status().is3xxRedirection());       
        
	}

}
