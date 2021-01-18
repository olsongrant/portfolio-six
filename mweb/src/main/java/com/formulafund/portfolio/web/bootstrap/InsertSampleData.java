package com.formulafund.portfolio.web.bootstrap;

import java.time.LocalDateTime;
import java.util.Optional;
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
import com.formulafund.portfolio.data.services.PasswordEncoderService;
import com.formulafund.portfolio.data.services.TickerService;
import com.formulafund.portfolio.data.services.TransactionService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.data.view.HoldingView;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InsertSampleData implements CommandLineRunner {
	
	private UserService userService;
	private AccountService accountService;
	private TickerService tickerService;
	private TransactionService transactionService;
	private IssuingCompanyService issuingCompanyService;
	private PasswordEncoderService encoderService;

	
	public InsertSampleData(
			UserService uService, 
			AccountService aService,
			TickerService tService,
			TransactionService txnService,
			IssuingCompanyService icService,
			PasswordEncoderService anEncoderService) {
		this.userService = uService;
		this.accountService = aService;
		this.tickerService = tService;
		this.transactionService = txnService;
		this.issuingCompanyService = icService;
		this.encoderService = anEncoderService;
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
		log.debug("InsertSampleData::run");
		IssuingCompany berkshireItself = this.issuingCompanyService.getInstanceFor("Berkshire Hathaway");
		Ticker berkshireB = this.tickerService.getInstanceFor("BRKB", berkshireItself, Exchange.NYSE );
		IssuingCompany alphabet = this.issuingCompanyService.getInstanceFor("Google");
		Ticker goog = this.tickerService.getInstanceFor("GOOG", alphabet, Exchange.NASDAQ);
		IssuingCompany microsoft = this.issuingCompanyService.getInstanceFor("Microsoft");
		Ticker msft = this.tickerService.getInstanceFor("MSFT", microsoft, Exchange.NASDAQ);	
		IssuingCompany netflix = this.issuingCompanyService.getInstanceFor("Netflix");
		Ticker nflx = this.tickerService.getInstanceFor("NFLX", netflix, Exchange.NASDAQ);
		IssuingCompany ibmCorp = this.issuingCompanyService.getInstanceFor("International Business Machines");
		Ticker ibm = this.tickerService.getInstanceFor("IBM", ibmCorp, Exchange.NYSE);
		ApplicationUser grantcine = null;
		Optional<ApplicationUser> possibleGrantcine = 
				this.userService.findByEmailAddress("grant@address.com");
		if (possibleGrantcine.isPresent()) {
			grantcine = possibleGrantcine.get();
			log.info("found a Grantcine user");
		} else {
			grantcine = new ApplicationUser();
			grantcine.setFirstName("Grantcine");
			grantcine.setLastName("Olson");
			grantcine.setHandle("grantcine");
			grantcine.setEmailAddress("grant@address.com");
			grantcine.setPassword(this.encoderService.encode("grantcine"));
			this.userService.save(grantcine);
			log.info("Saved a Grantcine user.");
			Account valueInvesting = Account.with("valueAccount", grantcine);
			Account growthInvesting = Account.with("growthInvesting", grantcine);
			this.accountService.save(valueInvesting);
			this.accountService.save(growthInvesting);
			log.info("valueInvesting cash immediately after account creation: " + 
					valueInvesting.getCurrentCash());
			BuyCommand buy = new BuyCommand();
			buy.setAccountId(valueInvesting.getId());
			buy.setSymbol("BRKB");
			buy.setShareQuantity(10.0f);
			
			this.accountService.buyAndReportRemainingCash(buy);
			valueInvesting = this.accountService.findById(valueInvesting.getId());
			log.info("valueInvesting cash immediately after buying BRKB: " + 
					valueInvesting.getCurrentCash());		
			buy = new BuyCommand();
			buy.setAccountId(growthInvesting.getId());
			buy.setSymbol("GOOG");
			buy.setShareQuantity(12.0f);
			this.accountService.buyAndReportRemainingCash(buy);

//			Transaction msftToday = Transaction.purchaseOf(msft, LocalDateTime.now().minusDays(2), valueInvesting, 40.0f);
//			this.transactionService.save(msftToday);
			buy = new BuyCommand();
			buy.setAccountId(valueInvesting.getId());
			buy.setSymbol("MSFT");
			buy.setShareQuantity(40.0f);
			this.accountService.buyAndReportRemainingCash(buy);
			valueInvesting = this.accountService.findById(valueInvesting.getId());
			log.info("valueInvesting cash immediately after buying MSFT: " + 
					valueInvesting.getCurrentCash());
			Set<Transaction> hodgePodgePurchases = this.transactionService.purchasesForAccount(valueInvesting); 
			log.info("stock purchases for the portfolio: ");
			hodgePodgePurchases.forEach(sp -> log.info(sp.toString()));

//			Transaction nflxPreviously = Transaction.purchaseOf(nflx, LocalDateTime.now().minusDays(2), growthInvesting, 100.0f);
//			this.transactionService.save(nflxPreviously);
			buy = new BuyCommand();
			buy.setAccountId(growthInvesting.getId());
			buy.setSymbol("NFLX");
			buy.setShareQuantity(100.0f);
			this.accountService.buyAndReportRemainingCash(buy);
			Set<StockHolding> holdings = this.accountService.getCurrentHoldings(valueInvesting);
			log.info("Before selling MSFT: ");
			holdings.forEach(h -> log.info(h.toString()));
			this.accountService.sellAndReportRemaining(msft, 40.0f, valueInvesting);
			log.info("After selling MSFT: ");
			valueInvesting = this.accountService.findById(valueInvesting.getId());
			log.info("valueInvesting cash immediately after selling MSFT: " + 
					valueInvesting.getCurrentCash());
			holdings = this.accountService.getCurrentHoldings(valueInvesting);
			holdings.forEach(h -> log.info(h.toString()));

//			Transaction ibmPreviously = Transaction.purchaseOf(ibm, LocalDateTime.now().minusDays(1), valueInvesting, 50.0f);
//			this.transactionService.save(ibmPreviously);
			buy = new BuyCommand();
			buy.setAccountId(valueInvesting.getId());
			buy.setSymbol("IBM");
			buy.setShareQuantity(50.0f);
			log.info("Cash in valueInvesting before IBM purchase: "+ valueInvesting.getCurrentCash());
			Float cashAfterIBMBuy = this.accountService.buyAndReportRemainingCash(buy);
			log.info("cash after IBM purchase -- returned from buyAndReportRemaining: " + cashAfterIBMBuy);
			valueInvesting = this.accountService.findById(valueInvesting.getId());
			log.info("Cash in valueInvesting after IBM purchase: "+ valueInvesting.getCurrentCash());
			this.accountService.sellAndReportRemaining(ibm, 25.0f, valueInvesting);
			log.info("After buying 50 shares of IBM and then immediately selling 25: ");
			valueInvesting = this.accountService.findById(valueInvesting.getId());
			log.info("valueInvesting cash immediately after selling half of IBM: " + 
					valueInvesting.getCurrentCash());
			holdings = this.accountService.getCurrentHoldings(valueInvesting);
			holdings.forEach(h -> log.info(h.toString()));
			Set<HoldingView> hViews = this.accountService.getHoldingsView("valueAccount");
			hViews.forEach(v -> log.info(v.toString()));
		}
		Optional<ApplicationUser> potentialDaffy = this.userService.findByEmailAddress("daffy@address.com");
		if (potentialDaffy.isEmpty()) {
			ApplicationUser daffy = ApplicationUser.with("Daffy", "Duck", "daffy");
			daffy.setEmailAddress("daffy@address.com");
			daffy.setPassword(this.encoderService.encode("ducksrule"));
			this.userService.save(daffy);		
			Account dayTrader = Account.with("dayTrades", daffy);
			this.accountService.save(dayTrader);
			Account etfTrader = Account.with("etfsOnly", daffy);
			this.accountService.save(etfTrader);
		}

	}
	
	

}
