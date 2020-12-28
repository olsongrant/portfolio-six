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

@Controller
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
		Set<Account> accountSet = user.getAccounts();
		model.addAttribute("accountSet", accountSet);
		return "user/show";
	}
	
	
}
