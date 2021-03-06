package com.formulafund.portfolio.data.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.formulafund.portfolio.data.model.*;


public interface TransactionService extends CrudService<Transaction> {
	
//	public Set<Transaction> purchasesForPortfolio(Portfolio aPortfolio); 
	
	public default Float getCurrentHoldingOf(Ticker aTicker, Account anAccount) {
		Set<Transaction> relatedTransactions = this.transactionsFor(anAccount, aTicker);
		Float netQuantity = relatedTransactions.stream()
				.reduce(Float.valueOf(0), TransactionService::addTransactionToExisting, (a, b) -> a + b);
		return netQuantity;
	}
	
	public default Set<Transaction> transactionsFor(Account anAccount, Ticker aTicker) {
		Set<Transaction> allTransactions = this.allTransactionsForAccount(anAccount);
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

	public default Set<Transaction> allPurchasesForPortfolio(Account anAccount) {
		Set<Transaction> purchasesForPortfolio = this.allTransactionsForAccount(anAccount)
				.stream()
				.filter(s -> TransactionType.PURCHASE.equals(s.getTransactionType()))
				.collect(Collectors.toSet());
			return purchasesForPortfolio;
	}

	public default Set<Transaction> allSales() {
		return this.findAll()
				.stream()
				.filter(t -> TransactionType.SALE.equals(t.getTransactionType()))
				.collect(Collectors.toSet());
	}
	
	public default Set<Transaction> allTransactionsForAccount(Account anAccount) {
		return this.findAll()
				.stream()
				.filter(t -> anAccount.equals(t.getAccount()))
				.collect(Collectors.toSet());
	}

	public default Set<Transaction> allSalesForAccount(Account anAccount) {
		Set<Transaction> salesForPortfolio = this.allTransactionsForAccount(anAccount)
			.stream()
			.filter(s -> TransactionType.SALE.equals(s.getTransactionType()))
			.collect(Collectors.toSet());
		return salesForPortfolio;
	}
	
	public default Set<Ticker> uniqueTickersForAccount(Account anAccount) {
		Set<Transaction> allPurchases = this.allPurchasesForPortfolio(anAccount);
		HashSet<Ticker> tickerSet = new HashSet<>();
		allPurchases.forEach(sp -> tickerSet.add(sp.getTicker()));
		return tickerSet;
	}
	
	public default Set<Transaction> salesForAccount(Account anAccount) {
		return this.allSalesForAccount(anAccount);
	}


	public default Set<Transaction> purchasesForAccount(Account anAccount) {
		Set<Transaction> purchases = this.allTransactionsForAccount(anAccount)
				.stream()
				.filter(t -> TransactionType.PURCHASE.equals(t.getTransactionType()))
				.collect(Collectors.toSet());
		return purchases;
	}
}
