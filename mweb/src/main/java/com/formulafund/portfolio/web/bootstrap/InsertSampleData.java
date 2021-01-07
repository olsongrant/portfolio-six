package com.formulafund.portfolio.web.bootstrap;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.formulafund.portfolio.data.commands.BuyCommand;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.Exchange;
import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.IssuingCompanyService;
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

	
	public InsertSampleData(
			UserService uService, 
			AccountService aService,
			TickerService tService,
			TransactionService txnService,
			IssuingCompanyService icService) {
		this.userService = uService;
		this.accountService = aService;
		this.tickerService = tService;
		this.transactionService = txnService;
		this.issuingCompanyService = icService;
	}

	@Override
	public void run(String... args) throws Exception {
		Set<Ticker> tickerSet = this.tickerService.findAll();
		if (tickerSet.size() < 1) {
			insertSampleInfo();
		}
	}

	@Transactional
	protected void insertSampleInfo() {
		System.out.println("InsertSampleData::run");
		ApplicationUser grant = new ApplicationUser();
		grant.setFirstName("Grant");
		grant.setLastName("Olson");
		grant.setHandle("grantcine");
		grant.setEmailAddress("email@address.com");
		grant.setPassword("aPassword");
		this.userService.save(grant);
		System.out.println("Saved a Grant Olson user.");
		ApplicationUser daffy = ApplicationUser.with("Daffy", "Duck", "daffy");
		daffy.setEmailAddress("daffy@address.com");
		daffy.setPassword("ducksrule");
		this.userService.save(daffy);
		Account valueInvesting = Account.with("valueAccount", grant);
		Account growthInvesting = Account.with("growthInvesting", grant);
		this.accountService.save(valueInvesting);
		this.accountService.save(growthInvesting);
		Account dayTrader = Account.with("dayTrades", daffy);
		this.accountService.save(dayTrader);
		Account etfTrader = Account.with("etfsOnly", daffy);
		this.accountService.save(etfTrader);
		IssuingCompany berkshireItself = this.issuingCompanyService.getInstanceFor("Berkshire Hathaway");
		Ticker berkshireB = this.tickerService.getInstanceFor("BRKB", berkshireItself, Exchange.NYSE );
		System.out.println("valueInvesting cash immediately after account creation: " + 
							valueInvesting.getCurrentCash());
		BuyCommand buy = new BuyCommand();
		buy.setAccountId(valueInvesting.getId());
		buy.setSymbol("BRKB");
		buy.setShareQuantity(10.0f);
//		Transaction berkshireToday = Transaction.purchaseOf(berkshireB, LocalDateTime.now().minusDays(2), valueInvesting, 10.0f);
//		this.transactionService.save(berkshireToday);
		this.accountService.buyAndReportRemainingCash(buy);
		valueInvesting = this.accountService.findById(valueInvesting.getId());
		System.out.println("valueInvesting cash immediately after buying BRKB: " + 
				valueInvesting.getCurrentCash());		
		IssuingCompany alphabet = this.issuingCompanyService.getInstanceFor("Google");
		Ticker goog = this.tickerService.getInstanceFor("GOOG", alphabet, Exchange.NASDAQ);
//		Transaction googToday = Transaction.purchaseOf(goog, LocalDateTime.now().minusDays(2), growthInvesting, 12.0f);
//		this.transactionService.save(googToday);
		buy = new BuyCommand();
		buy.setAccountId(growthInvesting.getId());
		buy.setSymbol("GOOG");
		buy.setShareQuantity(12.0f);
		this.accountService.buyAndReportRemainingCash(buy);
		IssuingCompany microsoft = this.issuingCompanyService.getInstanceFor("Microsoft");
		Ticker msft = this.tickerService.getInstanceFor("MSFT", microsoft, Exchange.NASDAQ);
//		Transaction msftToday = Transaction.purchaseOf(msft, LocalDateTime.now().minusDays(2), valueInvesting, 40.0f);
//		this.transactionService.save(msftToday);
		buy = new BuyCommand();
		buy.setAccountId(valueInvesting.getId());
		buy.setSymbol("MSFT");
		buy.setShareQuantity(40.0f);
		this.accountService.buyAndReportRemainingCash(buy);
		valueInvesting = this.accountService.findById(valueInvesting.getId());
		System.out.println("valueInvesting cash immediately after buying MSFT: " + 
				valueInvesting.getCurrentCash());
		Set<Transaction> hodgePodgePurchases = this.transactionService.purchasesForAccount(valueInvesting); 
		System.out.println("stock purchases for the portfolio: ");
		hodgePodgePurchases.forEach(sp -> System.out.println(sp.toString()));
		IssuingCompany netflix = this.issuingCompanyService.getInstanceFor("Netflix");
		Ticker nflx = this.tickerService.getInstanceFor("NFLX", netflix, Exchange.NASDAQ);
//		Transaction nflxPreviously = Transaction.purchaseOf(nflx, LocalDateTime.now().minusDays(2), growthInvesting, 100.0f);
//		this.transactionService.save(nflxPreviously);
		buy = new BuyCommand();
		buy.setAccountId(growthInvesting.getId());
		buy.setSymbol("NFLX");
		buy.setShareQuantity(100.0f);
		this.accountService.buyAndReportRemainingCash(buy);
		Set<StockHolding> holdings = this.accountService.getCurrentHoldings(valueInvesting);
		System.out.println("Before selling MSFT: ");
		holdings.forEach(h -> System.out.println(h.toString()));
		this.accountService.sellAndReportRemaining(msft, 40.0f, valueInvesting);
		System.out.println("After selling MSFT: ");
		valueInvesting = this.accountService.findById(valueInvesting.getId());
		System.out.println("valueInvesting cash immediately after selling MSFT: " + 
				valueInvesting.getCurrentCash());
		holdings = this.accountService.getCurrentHoldings(valueInvesting);
		holdings.forEach(h -> System.out.println(h.toString()));
		IssuingCompany ibmCorp = this.issuingCompanyService.getInstanceFor("International Business Machines");
		Ticker ibm = this.tickerService.getInstanceFor("IBM", ibmCorp, Exchange.NYSE);
//		Transaction ibmPreviously = Transaction.purchaseOf(ibm, LocalDateTime.now().minusDays(1), valueInvesting, 50.0f);
//		this.transactionService.save(ibmPreviously);
		buy = new BuyCommand();
		buy.setAccountId(valueInvesting.getId());
		buy.setSymbol("IBM");
		buy.setShareQuantity(50.0f);
		System.out.println("Cash in valueInvesting before IBM purchase: "+ valueInvesting.getCurrentCash());
		Float cashAfterIBMBuy = this.accountService.buyAndReportRemainingCash(buy);
		System.out.println("cash after IBM purchase -- returned from buyAndReportRemaining: " + cashAfterIBMBuy);
		valueInvesting = this.accountService.findById(valueInvesting.getId());
		System.out.println("Cash in valueInvesting after IBM purchase: "+ valueInvesting.getCurrentCash());
		this.accountService.sellAndReportRemaining(ibm, 25.0f, valueInvesting);
		System.out.println("After buying 50 shares of IBM and then immediately selling 25: ");
		valueInvesting = this.accountService.findById(valueInvesting.getId());
		System.out.println("valueInvesting cash immediately after selling half of IBM: " + 
				valueInvesting.getCurrentCash());
		holdings = this.accountService.getCurrentHoldings(valueInvesting);
		holdings.forEach(h -> System.out.println(h.toString()));
		Set<HoldingView> hViews = this.accountService.getHoldingsView("valueAccount");
		hViews.forEach(v -> System.out.println(v.toString()));
	}
	
	

}
