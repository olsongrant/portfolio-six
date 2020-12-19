package com.formulafund.portfolio.web.bootstrap;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.Exchange;
import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.Portfolio;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.User;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.IssuingCompanyService;
import com.formulafund.portfolio.data.services.PortfolioService;
import com.formulafund.portfolio.data.services.TickerService;
import com.formulafund.portfolio.data.services.TransactionService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.data.view.HoldingView;

@Component
public class InsertSampleData implements CommandLineRunner {
	
	private UserService userService;
	private AccountService accountService;
	private TickerService tickerService;
	private TransactionService transactionService;
	private IssuingCompanyService issuingCompanyService;
	private PortfolioService portfolioService;
	
	public InsertSampleData(
			UserService uService, 
			AccountService aService,
			TickerService tService,
			TransactionService txnService,
			IssuingCompanyService icService,
			PortfolioService aPortfolioService) {
		this.userService = uService;
		this.accountService = aService;
		this.tickerService = tService;
		this.transactionService = txnService;
		this.issuingCompanyService = icService;
		this.portfolioService = aPortfolioService;
	}

	@Override
	public void run(String... args) throws Exception {
		Set<Ticker> tickerSet = this.tickerService.findAll();
		if (tickerSet.size() < 1) {
			insertSampleInfo();
		}
	}

	protected void insertSampleInfo() {
		System.out.println("InsertSampleData::run");
		User grant = new User();
		grant.setFullName("Grant Olson");
		grant.setHandle("grantcine");
		this.userService.save(grant);
		System.out.println("Saved a Grant Olson user.");
		User daffy = User.with("Daffy Duck", "daffy");
		this.userService.save(daffy);
		Account valueInvesting = Account.with("valueAccount", grant);
		Account growthInvesting = Account.with("growthInvesting", grant);
		this.accountService.save(valueInvesting);
		this.accountService.save(growthInvesting);
		Account dayTrader = Account.with("dayTrades", daffy);
		this.accountService.save(dayTrader);
		Account etfTrader = Account.with("etfsOnly", daffy);
		this.accountService.save(etfTrader);
		Portfolio hodgePodge = new Portfolio();
		hodgePodge.setName("hodgePodge");
		hodgePodge.setAccount(dayTrader);
		this.portfolioService.save(hodgePodge);
		IssuingCompany berkshireItself = this.issuingCompanyService.getInstanceFor("Berkshire Hathaway");
		Ticker berkshireB = this.tickerService.getInstanceFor("BRKB", berkshireItself, Exchange.NYSE );
		Transaction berkshireToday = Transaction.purchaseOf(berkshireB, LocalDateTime.now().minusDays(2), hodgePodge, 10.0f);
		this.transactionService.save(berkshireToday);
		IssuingCompany alphabet = this.issuingCompanyService.getInstanceFor("Google");
		Ticker goog = this.tickerService.getInstanceFor("GOOG", alphabet, Exchange.NASDAQ);
		Transaction googToday = Transaction.purchaseOf(goog, LocalDateTime.now().minusDays(2), hodgePodge, 12.0f);
		this.transactionService.save(googToday);
		IssuingCompany microsoft = this.issuingCompanyService.getInstanceFor("Microsoft");
		Ticker msft = this.tickerService.getInstanceFor("MSFT", microsoft, Exchange.NASDAQ);
		Transaction msftToday = Transaction.purchaseOf(msft, LocalDateTime.now().minusDays(2), hodgePodge, 40.0f);
		this.transactionService.save(msftToday);
		Set<Portfolio> portfolioSet = this.portfolioService.findAll();
		System.out.println("portfolioSet: ");
		portfolioSet.forEach(p -> System.out.println(p.toString()));
		Set<Transaction> hodgePodgePurchases = this.transactionService.purchasesForPortfolio(hodgePodge); 
		System.out.println("stock purchases for the portfolio: ");
		hodgePodgePurchases.forEach(sp -> System.out.println(sp.toString()));
		IssuingCompany netflix = this.issuingCompanyService.getInstanceFor("Netflix");
		Ticker nflx = this.tickerService.getInstanceFor("NFLX", netflix, Exchange.NASDAQ);
		Transaction nflxPreviously = Transaction.purchaseOf(nflx, LocalDateTime.now().minusDays(2), hodgePodge, 100.0f);
		this.transactionService.save(nflxPreviously);
		Set<StockHolding> holdings = this.portfolioService.getCurrentHoldings(hodgePodge);
		System.out.println("Before selling MSFT: ");
		holdings.forEach(h -> System.out.println(h.toString()));
		this.portfolioService.sellAndReportRemaining(msft, 40.0f, hodgePodge);
		System.out.println("After selling MSFT: ");
		holdings = this.portfolioService.getCurrentHoldings(hodgePodge);
		holdings.forEach(h -> System.out.println(h.toString()));
		IssuingCompany ibmCorp = this.issuingCompanyService.getInstanceFor("International Business Machines");
		Ticker ibm = this.tickerService.getInstanceFor("IBM", ibmCorp, Exchange.NYSE);
		Transaction ibmPreviously = Transaction.purchaseOf(ibm, LocalDateTime.now().minusDays(1), hodgePodge, 50.0f);
		this.transactionService.save(ibmPreviously);
		this.portfolioService.sellAndReportRemaining(ibm, 25.0f, hodgePodge);
		System.out.println("After buying 50 shares of IBM and then immediately selling 25: ");
		holdings = this.portfolioService.getCurrentHoldings(hodgePodge);
		holdings.forEach(h -> System.out.println(h.toString()));
		Set<HoldingView> hViews = this.portfolioService.getHoldingsView("hodgePodge");
		hViews.forEach(v -> System.out.println(v.toString()));
	}
	
	

}
