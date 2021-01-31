package com.formulafund.portfolio.data.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formulafund.portfolio.data.commands.BuyCommand;
import com.formulafund.portfolio.data.commands.SaleCommand;
import com.formulafund.portfolio.data.exceptions.InadequatePositionException;
import com.formulafund.portfolio.data.exceptions.InsufficientFundsException;
import com.formulafund.portfolio.data.exceptions.TickerNotFoundException;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.view.HoldingView;




public interface AccountService extends CrudService<Account> {
	
	Logger log = LoggerFactory.getLogger(AccountService.class);

	Float getCurrentHoldingOf(Ticker aTicker, Account anAccount);
	Set<StockHolding> getCurrentHoldings(Account anAccount);
	Float sellAndReportRemaining(Ticker aTicker, Float quantity, Account anAccount);
	Float buyAndReportRemainingCash(BuyCommand aBuyCommand);
	Float sellAndReportRemainingCash(SaleCommand aSaleCommand);
	
	public default StockHolding stockHoldingFor(Ticker aTicker, Account anAccount) {
		Float quantity = this.getCurrentHoldingOf(aTicker, anAccount);
		return new StockHolding(aTicker, quantity);
	}
	

	public static Float getCurrentHoldingOf(TransactionService txnService, Ticker aTicker, Account anAccount) {
		Set<Transaction> relatedTransactions = txnService.transactionsFor(anAccount, aTicker);
		Float netQuantity = relatedTransactions.stream()
				.reduce(Float.valueOf(0), TransactionService::addTransactionToExisting, (a, b) -> a + b);
		return netQuantity;
	}

	public default Set<StockHolding> getCurrentHoldings(TransactionService txnService, Account anAccount) {
//		HashSet<StockHolding> holdings = new HashSet<>();
		Set<Ticker> uniqueTickers = txnService.uniqueTickersForAccount(anAccount);
		Set<StockHolding> holdings = uniqueTickers.stream()
			.map(ticker -> this.stockHoldingFor(ticker, anAccount))
			.collect(Collectors.toSet());
		holdings.removeIf(h -> h.getShareQuantity() <= 0.0f);
		return holdings;
	}
	
	public default Float sellAndReportRemaining(TransactionService txnService, 
												PriceService aPriceService,
												AccountService anAccountService,
												Ticker aTicker, 
												Float quantity, 
												Account anAccount) {
		if (quantity <= 0.0f) {
			throw new IllegalArgumentException("sale quantity must be greater than 0");
		}
		Float quantityOnHand = this.getCurrentHoldingOf(aTicker, anAccount);
		if (quantityOnHand >= quantity) {
			Float sharePrice = aPriceService.sharePriceForTicker(aTicker);
			Float cash = aPriceService.balanceAfterCredit(quantity, 
														  anAccount.getCurrentCash(), 
														  sharePrice);
			anAccount.setCurrentCash(cash);
			anAccountService.save(anAccount);
			Transaction aSale = Transaction.saleOf(aTicker, LocalDateTime.now(), anAccount, quantity);
			aSale.setSharePrice(sharePrice);
			txnService.save(aSale);
			return quantityOnHand - quantity;
		} else {
			throw new InadequatePositionException("Account only has " + 
					quantityOnHand + " shares, while the sale amount requested was " +
					quantity + " shares.");
			
		}
	}
	
	public default Float sellAndReportRemainingCash(TransactionService aTransactionService,
												    PriceService aPriceService,
												    AccountService anAccountService,
												    TickerService aTickerService,
												    SaleCommand aSaleCommand) {
		String symbol = aSaleCommand.getSymbol();
		Optional<Ticker> potentialTicker = aTickerService.findTickerBySymbol(symbol);
		if (potentialTicker.isEmpty()) {
			throw new TickerNotFoundException("search for ticker by symbol " + symbol + " failed.");
		}
		Ticker aTicker = potentialTicker.get();
		Long accountIdLong = Long.valueOf(aSaleCommand.getAccountId());
		Account account = anAccountService.findById(accountIdLong);
		Float aSaleQuantity = aSaleCommand.getSaleQuantity();
		return this.sellAndReportRemaining(aTransactionService, 
												aPriceService,
												anAccountService,
												aTicker, 
												aSaleQuantity, 
												account);
	}
	
	public default Float buyAndReportRemainingCash(TransactionService txnService, PriceService aPriceService,
			AccountService anAccountService, Ticker aTicker, Float quantity, Account anAccount) {
		if (quantity <= 0.0f) {
			throw new IllegalArgumentException("purchase quantity must be greater than 0");
		}
		Transaction aPurchase = Transaction.purchaseOf(aTicker, LocalDateTime.now(), anAccount, quantity);
		Float sharePrice = aPriceService.sharePriceForTicker(aTicker);
		aPurchase.setSharePrice(sharePrice);
		Float cash = aPriceService.balanceAfterDeduction(quantity, 
														 anAccount.getCurrentCash(), 
														 sharePrice);
		if (cash < 0.0f) {
			throw new InsufficientFundsException("inadequate amount of cash to buy this item.");
		}
			
		anAccount.setCurrentCash(cash);
		anAccountService.save(anAccount);

		txnService.save(aPurchase);
		return cash;

	}
	
	public default Set<HoldingView> getHoldingsView(String aName) {
		TreeSet<HoldingView> holdingViews = new TreeSet<>();
		Optional<Account> optional = this.findAccountByName(aName);
		Account account = null;
		try {
			account = optional.orElseThrow();
		} catch (Exception e) {
			return holdingViews;
		}
		return populateHoldingsViewForAccount(holdingViews, account);
	}
	
	public default Optional<Account> findAccountByName(String aName) {
		if (aName == null) throw new RuntimeException("null parameter sent in to MapPortfolioService::findPortfolioByName");
		Optional<Account> potentialAccount = this.findAll()
				.stream().filter(p -> aName.equalsIgnoreCase(p.getName())).findAny();
		return potentialAccount;
	}
	
	
	public default Set<HoldingView> getHoldingsView(Long accountId) {
		TreeSet<HoldingView> holdingViews = new TreeSet<>();
		Account account = this.findById(accountId);
		return populateHoldingsViewForAccount(holdingViews, account);
	}
	
	
	public default Set<HoldingView> populateHoldingsViewForAccount(TreeSet<HoldingView> holdingViews,
			Account account) {
		ApplicationUser user = account.getUser();
		HoldingView aView = new HoldingView();
		aView.setAccountName(account.getName());
		aView.setUserName(user.getFirstName().concat(" ").concat(user.getLastName()));
		Set<StockHolding> holdingSet = this.getCurrentHoldings(account);
		for (StockHolding h: holdingSet) {
			HoldingView hv = aView.deepCopy();
			hv.setTickerSymbol(h.getTicker().getSymbol());
			try {
				hv.setCompanyName(h.getTicker().getIssuingCompany().getFullName());
			} catch (Exception e) {
				log.debug("caught an exception when setting the issuing company name -- ignoring");
			}
			hv.setShareQuantity(h.getShareQuantity());
			holdingViews.add(hv);
		}
		if (holdingViews.size() > 0) {
			return holdingViews;
		} else {
//			holdingViews.add(aView);
			return holdingViews;
		}
	}
	

}
