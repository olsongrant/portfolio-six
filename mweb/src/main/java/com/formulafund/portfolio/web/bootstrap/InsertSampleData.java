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
import com.formulafund.portfolio.data.services.PasswordResetTokenService;
import com.formulafund.portfolio.data.services.PriceService;
import com.formulafund.portfolio.data.services.TickerService;
import com.formulafund.portfolio.data.services.TransactionService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.data.services.VerificationTokenService;
import com.formulafund.portfolio.data.view.HoldingView;
import com.formulafund.portfolio.web.controllers.RegistrationListener;

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
	private RegistrationListener registrationListener;
	private VerificationTokenService verificationTokenService;
	private PasswordResetTokenService passwordTokenService;
	private PriceService priceService;

	
	public InsertSampleData(
			UserService uService, 
			AccountService aService,
			TickerService tService,
			TransactionService txnService,
			IssuingCompanyService icService,
			PasswordEncoderService anEncoderService,
			RegistrationListener aRegistrationListener,
			VerificationTokenService aVerificationTokenService,
			PasswordResetTokenService aPasswordTokenService,
			PriceService aPriceService) {
		this.userService = uService;
		this.accountService = aService;
		this.tickerService = tService;
		this.transactionService = txnService;
		this.issuingCompanyService = icService;
		this.encoderService = anEncoderService;
		this.registrationListener = aRegistrationListener;
		this.verificationTokenService = aVerificationTokenService;
		this.passwordTokenService = aPasswordTokenService;
		this.priceService = aPriceService;
	}

	@Override
	public void run(String... args) throws Exception {
		Set<Ticker> tickerSet = this.tickerService.findAll();
		if (tickerSet.size() < 1) {
			insertSampleInfo();
		}
//		this.sendTestEmail();
	}

	protected void sendTestEmail() {
		this.registrationListener.sendTestEmail();
	}
	
	protected Ticker makeTicker(String companyName, String symbol, Exchange anExchange) {
		IssuingCompany company = this.issuingCompanyService.getInstanceFor(companyName);
		Ticker ticker = this.tickerService.getInstanceFor(symbol, company, anExchange);
		return ticker;
	}
	
	protected Float purchaseAnItem(Ticker ticker, Account account, Float quantity) {
		BuyCommand buy = BuyCommand.with(account.getId(), ticker.getSymbol(), quantity);
		return this.accountService.buyAndReportRemainingCash(buy);
	}
	
	protected Float takePosition(Ticker aTicker, Account anAccount, Float dollarAmount) {
		int shareQuantity = this.shareQuantityForPositionSize(aTicker.getSymbol(), dollarAmount);
		Float quantity = Float.valueOf(shareQuantity);
		return this.purchaseAnItem(aTicker, anAccount, quantity);
	}
	protected Account establishAccount(ApplicationUser aUser, String accountName) {
		Account account = Account.with(accountName, aUser);
		return this.accountService.save(account);	
	}
	
	protected int shareQuantityForPositionSize(String symbol, Float positionSizeTarget) {
		Float sharePrice = this.priceService.sharePriceForSymbol(symbol);
		Float targetQuantity = positionSizeTarget / sharePrice;
		return Math.round(targetQuantity);
	}
	
	protected final ApplicationUser ensureEnabledUser(String aFirstName, String aLastName,
										    String aHandle, String anEmailAddress,
										    String aPassword) {
		ApplicationUser user = null;
		Optional<ApplicationUser> potentialTargetUser = 
				this.userService.findByEmailAddress(anEmailAddress);
		if (potentialTargetUser.isPresent()) {
			return potentialTargetUser.get();
		}
		user = new ApplicationUser();
		user.setFirstName(aFirstName);
		user.setLastName(aLastName);
		user.setHandle(aHandle);
		user.setEmailAddress(anEmailAddress);
		user.setPassword(this.encoderService.encode(aPassword));
		user.setEnabled(true);
		return this.userService.save(user);
	}
	
	@Transactional
	protected void insertSampleInfo() {
		log.debug("InsertSampleData::run");
		Ticker gld = this.makeTicker("SPDR Gold Trust", "GLD", Exchange.NYSE);
		Ticker gbtc = this.makeTicker("Grayscale Bitcoin Trust", "GBTC", Exchange.OTC);
		Ticker emqq = this.makeTicker("The Emerging Markets Internet & Ecommerce ETF", "EMQQ", Exchange.NYSE);
		Ticker qqq = this.makeTicker("Invesco QQQ Trust Series 1", "QQQ", Exchange.NASDAQ);
		Ticker kba = this.makeTicker("KraneShares Bosera MSCI China A Share", "KBA", Exchange.NYSE);
		Ticker xbi = this.makeTicker("SPDR S&P Biotech ETF", "XBI", Exchange.NYSE);
		Ticker spy = this.makeTicker("SPDR S&P 500 ETF Trust", "SPY", Exchange.NYSE);
		Ticker iwn = this.makeTicker("iShares Russell 2000 Value ETF", "IWN", Exchange.NYSE);
		Ticker ijt = this.makeTicker("iShares S&P Small-Cap 600 Growth ETF", "IJT", Exchange.NASDAQ);
		Ticker gval = this.makeTicker("Cambria Global Value ETF", "GVAL", Exchange.BATS);
		
		if (this.userService.findByEmailAddress("sample@address.com").isEmpty()) {
			ApplicationUser sampleUser = this.ensureEnabledUser("Sample", 
					                                            "User", 
					                                            "sampleUser", 
					                                            "sample@address.com", 
					                                            "something-unexpected");
			Account sampleAccount = this.establishAccount(sampleUser, "evenly divided");
			Float targetDollarAmount = 9000.0f;
			this.takePosition(gld, sampleAccount, targetDollarAmount);
			this.takePosition(gbtc, sampleAccount, targetDollarAmount);
			this.takePosition(emqq, sampleAccount, targetDollarAmount);
			this.takePosition(qqq, sampleAccount, targetDollarAmount);
			this.takePosition(kba, sampleAccount, targetDollarAmount);
			this.takePosition(xbi, sampleAccount, targetDollarAmount);
			this.takePosition(spy, sampleAccount, targetDollarAmount);
			this.takePosition(iwn, sampleAccount, targetDollarAmount);
			this.takePosition(ijt, sampleAccount, targetDollarAmount);
			this.takePosition(gval, sampleAccount, targetDollarAmount);
		}
		
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
			grantcine.setEnabled(true);
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
			buy.setSymbol("IWN");
			buy.setShareQuantity(10.0f);
			
			this.accountService.buyAndReportRemainingCash(buy);
			valueInvesting = this.accountService.findById(valueInvesting.getId());
			log.info("valueInvesting cash immediately after buying IWN: " + 
					valueInvesting.getCurrentCash());		
			buy = new BuyCommand();
			buy.setAccountId(growthInvesting.getId());
			buy.setSymbol("QQQ");
			buy.setShareQuantity(12.0f);
			this.accountService.buyAndReportRemainingCash(buy);

			buy = new BuyCommand();
			buy.setAccountId(valueInvesting.getId());
			buy.setSymbol("SPY");
			buy.setShareQuantity(40.0f);
			this.accountService.buyAndReportRemainingCash(buy);
			valueInvesting = this.accountService.findById(valueInvesting.getId());
			log.info("valueInvesting cash immediately after buying SPY: " + 
					valueInvesting.getCurrentCash());
			Set<Transaction> hodgePodgePurchases = this.transactionService.purchasesForAccount(valueInvesting); 
			log.info("stock purchases for the portfolio: ");
			hodgePodgePurchases.forEach(sp -> log.info(sp.toString()));

			buy = new BuyCommand();
			buy.setAccountId(growthInvesting.getId());
			buy.setSymbol("KBA");
			buy.setShareQuantity(100.0f);
			this.accountService.buyAndReportRemainingCash(buy);
			Set<StockHolding> holdings = this.accountService.getCurrentHoldings(valueInvesting);
			log.info("Before selling SPY: ");
			holdings.forEach(h -> log.info(h.toString()));
			this.accountService.sellAndReportRemaining(spy, 40.0f, valueInvesting);
			log.info("After selling SPY: ");
			valueInvesting = this.accountService.findById(valueInvesting.getId());
			log.info("valueInvesting cash immediately after selling SPY: " + 
					valueInvesting.getCurrentCash());
			holdings = this.accountService.getCurrentHoldings(valueInvesting);
			holdings.forEach(h -> log.info(h.toString()));


			buy = new BuyCommand();
			buy.setAccountId(valueInvesting.getId());
			buy.setSymbol("EMQQ");
			buy.setShareQuantity(50.0f);
			log.info("Cash in valueInvesting before EMQQ purchase: "+ valueInvesting.getCurrentCash());
			Float cashAfterEMQQBuy = this.accountService.buyAndReportRemainingCash(buy);
			log.info("cash after IBM purchase -- returned from buyAndReportRemaining: " + cashAfterEMQQBuy);
			valueInvesting = this.accountService.findById(valueInvesting.getId());
			log.info("Cash in valueInvesting after EMQQ purchase: "+ valueInvesting.getCurrentCash());
			this.accountService.sellAndReportRemaining(emqq, 25.0f, valueInvesting);
			log.info("After buying 50 shares of IBM and then immediately selling 25: ");
			valueInvesting = this.accountService.findById(valueInvesting.getId());
			log.info("valueInvesting cash immediately after selling half of EMQQ: " + 
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
			daffy.setEnabled(true);
			this.userService.save(daffy);		
			Account dayTrader = Account.with("dayTrades", daffy);
			this.accountService.save(dayTrader);
			Account etfTrader = Account.with("etfsOnly", daffy);
			this.accountService.save(etfTrader);
		}

	}
	
	

}
