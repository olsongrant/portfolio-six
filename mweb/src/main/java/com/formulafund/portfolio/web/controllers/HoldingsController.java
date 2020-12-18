package com.formulafund.portfolio.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formulafund.portfolio.data.services.PortfolioService;

@Controller
public class HoldingsController {
	private PortfolioService portfolioService;
	
	public HoldingsController(PortfolioService ps) {
		this.portfolioService = ps;
	}
	
	@RequestMapping({"holdings/h", "holdings", "holdings/index", "holdings/"})
	public String getIndex(Model model) {
		System.out.println("holdings index page was requested");
		model.addAttribute("holdingSet", this.portfolioService.getHoldingsView("hodgePodge"));
		return "holdings/holdings";
	}

}
