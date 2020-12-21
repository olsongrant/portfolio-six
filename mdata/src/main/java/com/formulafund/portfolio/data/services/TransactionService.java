package com.formulafund.portfolio.data.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.formulafund.portfolio.data.model.*;


public interface TransactionService extends CrudService<Transaction> {
	
//	public Set<Transaction> purchasesForPortfolio(Portfolio aPortfolio); 
	
	public default Float getCurrentHoldingOf(Ticker aTicker, Portfolio aPortfolio) {
		Set<Transaction> relatedTransactions = this.transactionsFor(aPortfolio, aTicker);
		Float netQuantity = relatedTransactions.stream()
				.reduce(Float.valueOf(0), TransactionService::addTransactionToExisting, (a, b) -> a + b);
		return netQuantity;
	}
	
	public default Set<Transaction> transactionsFor(Portfolio aPortfolio, Ticker aTicker) {
		Set<Transaction> allTransactions = this.allTransactionsForPortfolio(aPortfolio);
		Set<Transaction> setForTicker = allTransactions
				.stream()
				.filter(txn -> aTicker.equals(txn.getTicker()))
				.collect(Collectors.toSet());
		return setForTicker;
	}
	
	public static Float addTransactionToExisting(Float incoming, Transaction txn) {
		if (TransactionType.PURCHASE.equals(txn.getTransactionType())) {
			return incoming + txn.getShareQuantity();
		} else {
			return incoming - txn.getShareQuantity();
		}
	}


	public default Set<Transaction> allPurchases() {
		return this.findAll()
			.stream()
			.filter(t -> TransactionType.PURCHASE.equals(t.getTransactionType()))
			.collect(Collectors.toSet());
	}

	public default Set<Transaction> allPurchasesForPortfolio(Portfolio aPortfolio) {
		Set<Transaction> purchases = this.allPurchases()
				.stream()
				.filter(p -> aPortfolio.equals(p.getPortfolio()))
				.collect(Collectors.toSet());
		return purchases;
	}

	public default Set<Transaction> allSales() {
		return this.findAll()
				.stream()
				.filter(t -> TransactionType.SALE.equals(t.getTransactionType()))
				.collect(Collectors.toSet());
	}
	
	public default Set<Transaction> allTransactionsForPortfolio(Portfolio aPortfolio) {
		return this.findAll()
				.stream()
				.filter(t -> aPortfolio.equals(t.getPortfolio()))
				.collect(Collectors.toSet());
	}

	public default Set<Transaction> allSalesForPortfolio(Portfolio aPortfolio) {
		Set<Transaction> salesForPortfolio = this.allSales()
			.stream()
			.filter(s -> aPortfolio.equals(s.getPortfolio()))
			.collect(Collectors.toSet());
		return salesForPortfolio;
	}
	
	public default Set<Ticker> uniqueTickersForPortfolio(Portfolio aPortfolio) {
		Set<Transaction> allPurchases = this.allPurchasesForPortfolio(aPortfolio);
		HashSet<Ticker> tickerSet = new HashSet<>();
		allPurchases.forEach(sp -> tickerSet.add(sp.getTicker()));
		return tickerSet;
	}
	
	public default Set<Transaction> salesForPortfolio(Portfolio aPortfolio) {
		Set<Transaction> sales = this.findAll()
			.stream()
			.filter(sp -> aPortfolio.equals(sp.getPortfolio()))
			.filter(t -> TransactionType.SALE.equals(t.getTransactionType()))
			.collect(Collectors.toSet());
		return sales;
	}


	public default Set<Transaction> purchasesForPortfolio(Portfolio aPortfolio) {
		Set<Transaction> purchases = this.findAll()
				.stream()
				.filter(n -> aPortfolio.equals(n.getPortfolio()))
				.filter(t -> TransactionType.PURCHASE.equals(t.getTransactionType()))
				.collect(Collectors.toSet());
		return purchases;
	}
}
