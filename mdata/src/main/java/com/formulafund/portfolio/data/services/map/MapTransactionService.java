package com.formulafund.portfolio.data.services.map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.services.TickerService;

@Service
@Profile("map")
public class MapTransactionService extends BaseMapService<Transaction>
		implements com.formulafund.portfolio.data.services.TransactionService {
	
	private TickerService tickerService;
	
	@Override
	public Transaction save(Transaction txn) {
		Ticker ticker = txn.getTicker();
		if (ticker == null) throw new RuntimeException("Ticker was null in MapTransactionService::save");
		if (!ticker.hasId()) {
			this.tickerService.save(ticker);
		}
		if (txn.getAccount() == null) throw new RuntimeException("Portfolio was null in MapTransactionsService::save");
		if (!txn.getAccount().hasId()) throw new RuntimeException("Portfolio was not saved in MapTransactionService::save");
		return super.save(txn);
	}

	public MapTransactionService(TickerService t) {
		this.tickerService = t;
	}





}
