package com.formulafund.portfolio.web.controllers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formulafund.portfolio.data.commands.BuyCommand;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.PriceService;
import com.formulafund.portfolio.data.services.TickerService;
import com.formulafund.portfolio.data.services.TransactionService;
import com.formulafund.portfolio.data.view.HoldingView;
import com.formulafund.portfolio.web.commands.AddAccountCommand;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class TransactionController {
	
	private AccountService accountService;
	private TransactionService transactionService;
	private TickerService tickerService;
	private PriceService priceService;
	
	public TransactionController(AccountService aService,
								 TransactionService tService,
								 TickerService tcService,
								 PriceService pService) {
		this.accountService = aService;
		this.transactionService = tService;
		this.tickerService = tcService;
		this.priceService = pService;
	}
	
	@Transactional
    @PostMapping("purchase")
    public String purchaseStock(@Valid @ModelAttribute("buy") BuyCommand command,
    							@ModelAttribute("account") Account account,
    							@ModelAttribute("user") ApplicationUser user,
    							BindingResult bindingResult) {
    	String destination = "redirect:/holdings/" + command.getAccountId() + "/show";
    	log.info("TransactionController::purchaseStock");
    	log.info("BuyCommand: " + command);
    	if (bindingResult.hasErrors()) {
    		log.info("purchaseStock experienced errors");
    		return "transaction/purchase";
    	}
    	this.accountService.buyAndReportRemainingCash(command);
        return destination;
    }

	@GetMapping("/holdings/{id}/buy")
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
