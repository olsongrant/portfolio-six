package com.formulafund.portfolio.data.services.map;

import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.TransactionService;

@Service
@Profile("map")
public class MapAccountService extends BaseMapService<Account> implements AccountService {
	
	private TransactionService transactionService;
	
	public MapAccountService(TransactionService tService) {
		this.transactionService = tService;
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
		return this.sellAndReportRemaining(this.transactionService, aTicker, quantity, anAccount);
	}

}
