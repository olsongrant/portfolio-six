package com.formulafund.portfolio.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formulafund.portfolio.data.services.AccountService;

@Controller
public class UserController {
	
	private AccountService accountService;
	
	public UserController(AccountService aService) {
		this.accountService = aService;
	}
	
	@RequestMapping({"user", "user/index"})
	public String getIndex(Model model) {
		System.out.println("user index page was requested");
		model.addAttribute("accountSet", this.accountService.findAll());
		return "user/index";
	}
	
	
}
