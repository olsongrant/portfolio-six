package com.formulafund.portfolio.web.controllers;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.User;
import com.formulafund.portfolio.data.services.PortfolioService;
import com.formulafund.portfolio.data.view.HoldingView;

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
	
	@RequestMapping("/holdings/{id}/show")
	public String showAccountsForUser(@PathVariable String id, Model model) {
		System.out.println("show holdings for account " + id);
		Long idLong = Long.valueOf(id);
		Set<HoldingView> holdingSet = this.portfolioService.getHoldingsView(idLong);
		model.addAttribute("holdingSet", holdingSet);
		return "holdings/show";
	}

}
