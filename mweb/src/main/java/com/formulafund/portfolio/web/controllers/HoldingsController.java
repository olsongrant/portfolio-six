package com.formulafund.portfolio.web.controllers;

import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formulafund.portfolio.data.commands.BuyCommand;
import com.formulafund.portfolio.data.commands.SaleCommand;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.TickerService;
import com.formulafund.portfolio.data.view.HoldingView;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HoldingsController {
	private AccountService accountService;
	private TickerService tickerService;
	
	public HoldingsController(AccountService ps,
							  TickerService aTickerService) {
		this.accountService = ps;
		this.tickerService = aTickerService;
	}
	
	@GetMapping({"holdings/h", "holdings", "holdings/index", "holdings/"})
	public String getIndex(Model model) {
		log.debug("holdings index page was requested");
		model.addAttribute("holdingSet", this.accountService.getHoldingsView("hodgePodge"));
		return "holdings/holdings";
	}
	
	@GetMapping("/holdings/{id}/show")
	public String showAccountsForUser(@PathVariable String id, Model model, HttpServletRequest request) {
		log.debug("show holdings for account " + id);
		Long idLong = Long.valueOf(id);
		Account account = this.accountService.findById(idLong);
		ApplicationUser user = account.getUser();
		if ((request.getRemoteUser() != null) && (request.getRemoteUser().equals(user.getEmailAddress()))) {
			model.addAttribute("allowAdd", true);
		}
		model.addAttribute("account", account);
		model.addAttribute("user", account.getUser());
		Set<HoldingView> holdingSet = this.accountService.getHoldingsView(idLong);
		model.addAttribute("holdingSet", holdingSet);
		return "holdings/show";
	}

	@GetMapping("/holdings/{accountid}/sell/{symbol}")
	public String prepareSaleForm(@PathVariable String accountid, 
								  @PathVariable String symbol,
								  Model model,
								  Authentication authentication) {
		log.debug("prepare sale form for account: " + accountid);
		Long accountIdLong = Long.valueOf(accountid);
		Account account = this.accountService.findById(accountIdLong);
		ApplicationUser user = account.getUser();
		if (authentication != null) {
			log.info("prepareSaleForm authentication name: " + authentication.getName());
		}
//		if ((request.getRemoteUser() != null) && (request.getRemoteUser().equals(user.getEmailAddress()))) {
//			model.addAttribute("resultmessage", "Unexpected: remote user name not same as logged-in user email address. "
//					+ "Recommendation: try logging out and then logging back in.");
//			return "simple";
//		}
		model.addAttribute("account", account);
		model.addAttribute("user", account.getUser());
		Set<HoldingView> holdingSet = this.accountService.getHoldingsView(accountIdLong);
		model.addAttribute("holdingSet", holdingSet);
		Optional<Ticker> potentialTicker = this.tickerService.findTickerBySymbol(symbol);
		if (potentialTicker.isEmpty()) {
			model.addAttribute("resultmessage", "Unexpected: couldn't get ticker record for symbol. "
					+ "Recommendation: try again later!");
			return "simple";
		}
		Ticker ticker = potentialTicker.get();
		Float amountHeld = this.accountService.getCurrentHoldingOf(ticker, account);
		model.addAttribute("amountHeld", amountHeld);
		SaleCommand saleCommand = new SaleCommand();
		saleCommand.setAccountId(accountIdLong);
		saleCommand.setCurrentQuantity(amountHeld);
		saleCommand.setSymbol(symbol);
		model.addAttribute("sale", saleCommand);
		return "transaction/saleForm";
	}

	
}
