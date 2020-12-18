package com.formulafund.portfolio.data.services.map;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.TransactionType;
import com.formulafund.portfolio.data.model.User;

import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.StockPurchase;
import com.formulafund.portfolio.data.model.StockSale;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.Portfolio;
import com.formulafund.portfolio.data.services.InadequatePositionException;
import com.formulafund.portfolio.data.services.PortfolioService;
import com.formulafund.portfolio.data.services.StockPurchaseService;
import com.formulafund.portfolio.data.services.StockSaleService;
import com.formulafund.portfolio.data.view.HoldingView;

@Service
public class MapPortfolioService extends BaseMapService<Portfolio> implements PortfolioService {
	private StockSaleService stockSaleService;
	private StockPurchaseService stockPurchaseService;

	
	public MapPortfolioService(StockSaleService aSaleService, StockPurchaseService aPurchaseService) {
		this.stockSaleService = aSaleService;
		this.stockPurchaseService = aPurchaseService;
	}

	@Override
	public Float getCurrentHoldingOf(Ticker aTicker, Portfolio aPortfolio) {
		Set<Transaction> relatedTransactions = this.transactionsFor(aPortfolio, aTicker);
		Float netQuantity = relatedTransactions.stream()
				.reduce(Float.valueOf(0), MapPortfolioService::addTransactionToExisting, (a, b) -> a + b);
		return netQuantity;
	}
	
	public StockHolding stockHoldingFor(Ticker aTicker, Portfolio aPortfolio) {
		Float quantity = this.getCurrentHoldingOf(aTicker, aPortfolio);
		StockHolding sh = new StockHolding(aTicker, quantity);
		return sh;
	}
	
	protected static Float addTransactionToExisting(Float incoming, Transaction txn) {
		if (TransactionType.PURCHASE.equals(txn.getTransactionType())) {
			return incoming + txn.getShareQuantity();
		} else {
			return incoming - txn.getShareQuantity();
		}
	}
	
	public Set<Ticker> uniqueTickersForPortfolio(Portfolio aPortfolio) {
		Set<StockPurchase> allPurchases = this.allPurchasesForPortfolio(aPortfolio);
		HashSet<Ticker> tickerSet = new HashSet<>();
		allPurchases.forEach(sp -> tickerSet.add(sp.getTicker()));
		return tickerSet;
	}

	@Override
	public Set<StockHolding> getCurrentHoldings(Portfolio aPortfolio) {
//		HashSet<StockHolding> holdings = new HashSet<>();
		Set<Ticker> uniqueTickers = this.uniqueTickersForPortfolio(aPortfolio);
		Set<StockHolding> holdings = uniqueTickers.stream()
			.map(ticker -> this.stockHoldingFor(ticker, aPortfolio))
			.collect(Collectors.toSet());
		holdings.removeIf(h -> h.getShareQuantity() <= 0.0f);
		return holdings;
	}
	
	
	protected Set<Transaction> transactionsFor(Portfolio aPortfolio, Ticker aTicker) {
		Set<Transaction> allTransactions = this.allTransactionsForPortfolio(aPortfolio);
		Set<Transaction> setForTicker = allTransactions
				.stream()
				.filter(txn -> aTicker.equals(txn.getTicker()))
				.collect(Collectors.toSet());
		return setForTicker;
	}

	@Override
	public Set<StockPurchase> allPurchases() {
		return this.stockPurchaseService.findAll();
	}

	@Override
	public Set<StockPurchase> allPurchasesForPortfolio(Portfolio aPortfolio) {
		Set<StockPurchase> purchases = this.allPurchases()
				.stream()
				.filter(p -> aPortfolio.equals(p.getPortfolio()))
				.collect(Collectors.toSet());
		return purchases;
	}

	@Override
	public Set<StockSale> allSales() {
		return this.stockSaleService.findAll();
	}
	
	public Set<Transaction> allTransactionsForPortfolio(Portfolio aPortfolio) {
		HashSet<Transaction> transactionSet = new HashSet<>();
		transactionSet.addAll((Collection<? extends Transaction>) this.allPurchasesForPortfolio(aPortfolio));
		transactionSet.addAll((Collection<? extends Transaction>) this.allSalesForPortfolio(aPortfolio));
		return transactionSet;
	}

	@Override
	public Set<StockSale> allSalesForPortfolio(Portfolio aPortfolio) {
		Set<StockSale> salesForPortfolio = this.allSales()
			.stream()
			.filter(s -> aPortfolio.equals(s.getPortfolio()))
			.collect(Collectors.toSet());
		return salesForPortfolio;
	}

	@Override
	public Float sellAndReportRemaining(Ticker aTicker, Float quantity, Portfolio aPortfolio) {
		if (quantity <= 0.0f) {
			throw new IllegalArgumentException("sale quantity must be greater than 0");
		}
		Float quantityOnHand = this.getCurrentHoldingOf(aTicker, aPortfolio);
		if (quantityOnHand >= quantity) {
			StockSale aSale = StockSale.of(aTicker, LocalDateTime.now(), aPortfolio, quantity);
			this.stockSaleService.save(aSale);
			return quantityOnHand - quantity;
		} else {
			throw new InadequatePositionException("Portfolio only has " + 
					quantityOnHand + " shares, while the sale amount requested was " +
					quantity + " shares.");
			
		}
	}

	@Override
	public Set<HoldingView> getHoldingsView(String aName) {
		TreeSet<HoldingView> holdingViews = new TreeSet<>();
		Optional<Portfolio> optional = this.findPortfolioByName(aName);
		Portfolio portfolio = null;
		try {
			portfolio = optional.orElseThrow();
		} catch (Exception e) {
			return holdingViews;
		}
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

	@Override
	public Optional<Portfolio> findPortfolioByName(String aName) {
		if (aName == null) throw new RuntimeException("null parameter sent in to MapPortfolioService::findPortfolioByName");
		Optional<Portfolio> potentialPortfolio = this.map.values()
				.stream().filter(p -> aName.equalsIgnoreCase(p.getName())).findAny();
		return potentialPortfolio;
	}

}
