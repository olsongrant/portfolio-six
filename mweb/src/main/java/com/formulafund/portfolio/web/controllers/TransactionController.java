package com.formulafund.portfolio.web.controllers;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.formulafund.portfolio.data.commands.BuyCommand;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.User;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.PriceService;
import com.formulafund.portfolio.data.services.TickerService;
import com.formulafund.portfolio.data.services.TransactionService;
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
    public String purchaseStock(@ModelAttribute BuyCommand command){
    	String destination = "redirect:/holdings/" + command.getAccountId() + "/show";
    	System.out.println("TransactionController::purchaseStock");
    	System.out.println("BuyCommand: " + command);
    	this.accountService.buyAndReportRemainingCash(command);
   /* 	Long idLong = Long.valueOf(command.getAccountId());
    	Account account = this.accountService.findById(idLong);
    	Optional<Ticker> potentialTicker = this.tickerService.findTickerBySymbol(command.getSymbol());
    	if (potentialTicker.isEmpty()) {
    		log.debug("ticker was not found by symbol");
    		return destination;
    	}
    	Ticker ticker = potentialTicker.get();
    	Float roundedNewCurrent = this.priceService.getPriceAndDeduct(command.getShareQuantity(), account.getCurrentCash(), ticker);
    	account.setCurrentCash(roundedNewCurrent);
    	this.accountService.save(account);
    	Transaction txn = Transaction.purchaseOf(ticker, LocalDateTime.now(), account, command.getShareQuantity());
    	this.transactionService.save(txn); */
        return destination;
    }


}
