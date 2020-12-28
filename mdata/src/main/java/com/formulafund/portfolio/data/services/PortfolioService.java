package com.formulafund.portfolio.data.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.Portfolio;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.User;
import com.formulafund.portfolio.data.services.map.MapPortfolioService;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.view.HoldingView;

public interface PortfolioService extends CrudService<Portfolio> {
	
	Float getCurrentHoldingOf(Ticker aTicker, Portfolio aPortfolio);
	Set<StockHolding> getCurrentHoldings(Portfolio aPortfolio);
//	Set<Transaction> allPurchases();
//	Set<Transaction> allPurchasesForPortfolio(Portfolio aPortfolio);
//	Set<Transaction> allSales();
//	Set<Transaction> allSalesForPortfolio(Portfolio aPortfolio);
	Float sellAndReportRemaining(Ticker aTicker, Float quantity, Portfolio aPortfolio);
//	Set<HoldingView> getHoldingsView(String portfolioName);
//	Optional<Portfolio> findPortfolioByName(String aName);
	
	public default StockHolding stockHoldingFor(Ticker aTicker, Portfolio aPortfolio) {
		Float quantity = this.getCurrentHoldingOf(aTicker, aPortfolio);
		return new StockHolding(aTicker, quantity);
	}
	

	public static Float getCurrentHoldingOf(TransactionService txnService, Ticker aTicker, Portfolio aPortfolio) {
		Set<Transaction> relatedTransactions = txnService.transactionsFor(aPortfolio, aTicker);
		Float netQuantity = relatedTransactions.stream()
				.reduce(Float.valueOf(0), TransactionService::addTransactionToExisting, (a, b) -> a + b);
		return netQuantity;
	}

	public default Set<StockHolding> getCurrentHoldings(TransactionService txnService, Portfolio aPortfolio) {
//		HashSet<StockHolding> holdings = new HashSet<>();
		Set<Ticker> uniqueTickers = txnService.uniqueTickersForPortfolio(aPortfolio);
		Set<StockHolding> holdings = uniqueTickers.stream()
			.map(ticker -> this.stockHoldingFor(ticker, aPortfolio))
			.collect(Collectors.toSet());
		holdings.removeIf(h -> h.getShareQuantity() <= 0.0f);
		return holdings;
	}
	
	public default Float sellAndReportRemaining(TransactionService txnService, Ticker aTicker, Float quantity, Portfolio aPortfolio) {
		if (quantity <= 0.0f) {
			throw new IllegalArgumentException("sale quantity must be greater than 0");
		}
		Float quantityOnHand = this.getCurrentHoldingOf(aTicker, aPortfolio);
		if (quantityOnHand >= quantity) {
			Transaction aSale = Transaction.saleOf(aTicker, LocalDateTime.now(), aPortfolio, quantity);
			txnService.save(aSale);
			return quantityOnHand - quantity;
		} else {
			throw new InadequatePositionException("Portfolio only has " + 
					quantityOnHand + " shares, while the sale amount requested was " +
					quantity + " shares.");
			
		}
	}
	
	public default Set<HoldingView> getHoldingsView(String aName) {
		TreeSet<HoldingView> holdingViews = new TreeSet<>();
		Optional<Portfolio> optional = this.findPortfolioByName(aName);
		Portfolio portfolio = null;
		try {
			portfolio = optional.orElseThrow();
		} catch (Exception e) {
			return holdingViews;
		}
		return populateHoldingsViewForPortfolio(holdingViews, portfolio);
	}
	
	public default Set<HoldingView> getHoldingsView(Long accountId) {
		TreeSet<HoldingView> holdingViews = new TreeSet<>();
		Optional<Portfolio> optional = this.findPortfolioByAccountId(accountId);
		Portfolio portfolio = null;
		try {
			portfolio = optional.orElseThrow();
		} catch (Exception e) {
			return holdingViews;
		}
		return populateHoldingsViewForPortfolio(holdingViews, portfolio);
	}
	
	
	public default Set<HoldingView> populateHoldingsViewForPortfolio(TreeSet<HoldingView> holdingViews,
			Portfolio portfolio) {
		Account account = portfolio.getAccount();
		User user = account.getUser();
		HoldingView aView = new HoldingView();
		aView.setAccountName(account.getName());
		aView.setUserName(user.getFullName());
		aView.setPortfolioName(portfolio.getName());
		Set<StockHolding> holdingSet = this.getCurrentHoldings(portfolio);
		for (StockHolding h: holdingSet) {
			HoldingView hv = aView.deepCopy();
			hv.setTickerSymbol(h.getTicker().getSymbol());
			try {
				hv.setCompanyName(h.getTicker().getIssuingCompany().getFullName());
			} catch (Exception e) {
				System.out.println("caught an exception when setting the issuing company name -- ignoring");
			}
			hv.setShareQuantity(h.getShareQuantity());
			holdingViews.add(hv);
		}
		if (holdingViews.size() > 0) {
			return holdingViews;
		} else {
			holdingViews.add(aView);
			return holdingViews;
		}
	}
	
	public default Optional<Portfolio> findPortfolioByAccountId(Long idLong) {
		if (idLong == null) throw new RuntimeException("null parameter sent in to MapPortfolioService::findPortfolioByAccountId");
		Optional<Portfolio> potentialPortfolio = this.findAll()
				.stream().filter(p -> idLong.equals(p.getAccount().getId())).findAny();
		return potentialPortfolio;		
	}

	public default Optional<Portfolio> findPortfolioByName(String aName) {
		if (aName == null) throw new RuntimeException("null parameter sent in to MapPortfolioService::findPortfolioByName");
		Optional<Portfolio> potentialPortfolio = this.findAll()
				.stream().filter(p -> aName.equalsIgnoreCase(p.getName())).findAny();
		return potentialPortfolio;
	}
}
