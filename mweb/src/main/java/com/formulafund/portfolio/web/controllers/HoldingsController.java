package com.formulafund.portfolio.web.controllers;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formulafund.portfolio.data.commands.BuyCommand;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.view.HoldingView;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HoldingsController {
	private AccountService accountService;
	
	public HoldingsController(AccountService ps) {
		this.accountService = ps;
	}
	
	@RequestMapping({"holdings/h", "holdings", "holdings/index", "holdings/"})
	public String getIndex(Model model) {
		log.debug("holdings index page was requested");
		model.addAttribute("holdingSet", this.accountService.getHoldingsView("hodgePodge"));
		return "holdings/holdings";
	}
	
	@RequestMapping("/holdings/{id}/show")
	public String showAccountsForUser(@PathVariable String id, Model model) {
		log.debug("show holdings for account " + id);
		Long idLong = Long.valueOf(id);
		Account account = this.accountService.findById(idLong);
		model.addAttribute("account", account);
		model.addAttribute("user", account.getUser());
		Set<HoldingView> holdingSet = this.accountService.getHoldingsView(idLong);
		model.addAttribute("holdingSet", holdingSet);
		return "holdings/show";
	}

	@RequestMapping("/holdings/{id}/buy")
	public String provideBuyForm(@PathVariable String id, Model model) {
		log.debug("provide purchase form for account " + id);
		BuyCommand buy = new BuyCommand();
		buy.setAccountId(id);
		model.addAttribute("buy", buy);
		Long idLong = Long.valueOf(id);
		Account account = this.accountService.findById(idLong);
		model.addAttribute("account", account);
		model.addAttribute("user", account.getUser());
		Set<HoldingView> holdingSet = this.accountService.getHoldingsView(idLong);
		model.addAttribute("holdingSet", holdingSet);
		return "transaction/purchase";
	}
	
}
