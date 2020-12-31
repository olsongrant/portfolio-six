package com.formulafund.portfolio.data.services.map;

import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.commands.BuyCommand;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.PriceService;
import com.formulafund.portfolio.data.services.TickerService;
import com.formulafund.portfolio.data.services.TransactionService;

@Service
@Profile("map")
public class MapAccountService extends BaseMapService<Account> implements AccountService {
	
	private TransactionService transactionService;
	private PriceService priceService;
	private TickerService tickerService;
	
	public MapAccountService(TransactionService tService,
							 PriceService aPriceService,
							 TickerService aTickerService) {
		this.transactionService = tService;
		this.priceService = aPriceService;
		this.tickerService = aTickerService;
	}
	
	@Override
	public Float getCurrentHoldingOf(Ticker aTicker, Account anAccount) {
		return AccountService.getCurrentHoldingOf(this.transactionService, aTicker, anAccount);
	}


	@Override
	public Set<StockHolding> getCurrentHoldings(Account anAccount) {	
		return this.getCurrentHoldings(this.transactionService, anAccount);
	}


	@Override
	public Float sellAndReportRemaining(Ticker aTicker, Float quantity, Account anAccount) {
		return this.sellAndReportRemaining(this.transactionService, 
										   this.priceService,
										   this,
										   aTicker, 
										   quantity, 
										   anAccount);
	}
	@Override
	public Float buyAndReportRemainingCash(BuyCommand aBuyCommand) {
		Long idLong = Long.valueOf(aBuyCommand.getAccountId());
		Account account = this.findById(idLong);
		Ticker ticker = this.tickerService.findTickerBySymbol(aBuyCommand.getSymbol()).orElseThrow();
		return this.buyAndReportRemainingCash(this.transactionService, 
											  this.priceService, 
											  this, 
											  ticker, 
											  aBuyCommand.getShareQuantity(), 
											  account);
	}

}
