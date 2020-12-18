package com.formulafund.portfolio.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formulafund.portfolio.data.services.UserService;

@Controller
public class IndexController {
	private UserService userService;
	
	public IndexController(UserService aService) {
		this.userService = aService;
	}
	
	@RequestMapping({"", "/", "index", "index.html"})
	public String getIndex(Model model) {
		System.out.println("root index page was requested");
		model.addAttribute("userSet", this.userService.findAll());
		return "index";
	}
}
