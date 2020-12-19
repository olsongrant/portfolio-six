package com.formulafund.portfolio.data.services.map;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Portfolio;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.TransactionType;
import com.formulafund.portfolio.data.services.TickerService;

@Service
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
		if (txn.getPortfolio() == null) throw new RuntimeException("Portfolio was null in MapTransactionsService::save");
		if (!txn.getPortfolio().hasId()) throw new RuntimeException("Portfolio was not saved in MapTransactionService::save");
		return super.save(txn);
	}

	public MapTransactionService(TickerService t) {
		this.tickerService = t;
	}

	public Set<Transaction> salesForPortfolio(Portfolio aPortfolio) {
		Set<Transaction> sales = this.map.values()
			.stream()
			.filter(sp -> aPortfolio.equals(sp.getPortfolio()))
			.filter(t -> TransactionType.SALE.equals(t.getTransactionType()))
			.collect(Collectors.toSet());
		return sales;
	}

	@Override
	public Set<Transaction> purchasesForPortfolio(Portfolio aPortfolio) {
		Set<Transaction> purchases = this.map.values()
				.stream()
				.filter(n -> aPortfolio.equals(n.getPortfolio()))
				.filter(t -> TransactionType.PURCHASE.equals(t.getTransactionType()))
				.collect(Collectors.toSet());
		return purchases;
	}



}
