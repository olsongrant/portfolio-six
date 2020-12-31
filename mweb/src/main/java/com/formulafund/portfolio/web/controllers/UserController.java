package com.formulafund.portfolio.web.controllers;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.User;
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

	
	public UserController(AccountService aService, UserService uService) {
		this.accountService = aService;
		this.userService = uService;
	}
	
	@RequestMapping({"user", "user/index"})
	public String getIndex(Model model) {
		System.out.println("user index page was requested");
		model.addAttribute("accountSet", this.accountService.findAll());
		return "user/index";
	}
	
	@RequestMapping("/user/{id}/show")
	public String showAccountsForUser(@PathVariable String id, Model model) {
		System.out.println("show user " + id);
		Long idLong = Long.valueOf(id);
		User user = this.userService.findById(idLong);
		model.addAttribute("user", user);
		Set<Account> accountSet = user.getAccounts();
		model.addAttribute("accountSet", accountSet);
		return "user/show";
	}
	
	@RequestMapping("/user/{id}/addaccount")
	public String provideAddAccountForm(@PathVariable String id, Model model) {
		log.debug("provide Add Account Form for user id " + id);
		Long idLong = Long.valueOf(id);
		User user = this.userService.findById(idLong);
		AddAccountCommand command = new AddAccountCommand();
		command.setId(id);
		command.setUserFullName(user.getFullName());
		command.setUserHandle(user.getHandle());
		model.addAttribute("addaccount", command);
		return "user/accountadd";
	}
	
}
