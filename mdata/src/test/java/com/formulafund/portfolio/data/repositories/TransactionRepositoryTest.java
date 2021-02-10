package com.formulafund.portfolio.data.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.Exchange;
import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.TransactionType;

@DataJpaTest
class TransactionRepositoryTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	IssuingCompanyRepository issuingCompanyRepository;
	
	@Autowired
	TickerRepository tickerRepository;
	
	Long emailUserId;
	Long accountId;
	Long issuingCompanyId;
	Long tickerId;
	

	@BeforeEach
	void setUp() throws Exception {
		ApplicationUser emailUser = ApplicationUser.with("Joey", "Bagadonuts", "bagadonuts");
		emailUser.setEmailAddress("joey.b@address.com");
		emailUser.setEnabled(true);
		emailUser = this.userRepository.save(emailUser);
		this.emailUserId = emailUser.getId();
		Account account = Account.with("fasttrade", emailUser);
		account = this.accountRepository.save(account);
		this.accountId = account.getId();
		IssuingCompany issuingCompany = new IssuingCompany();
		issuingCompany.setFullName("Acme Rocket Launchers");
		issuingCompany = this.issuingCompanyRepository.save(issuingCompany);
		this.issuingCompanyId = issuingCompany.getId();
		Ticker ticker = new Ticker();
		ticker.setExchange(Exchange.OTC);
		ticker.setIssuingCompany(issuingCompany);
		ticker.setSymbol("ACMERL");
		ticker = this.tickerRepository.save(ticker);
		this.tickerId = ticker.getId();
		Transaction purchase = new Transaction();
		purchase.setAccount(account);
		purchase.setSharePrice(10.0f);
		purchase.setShareQuantity(100.0f);
		purchase.setTicker(ticker);
		purchase.setTransactionDateTime(LocalDateTime.now());
		purchase.setTransactionType(TransactionType.PURCHASE);
		this.transactionRepository.save(purchase);
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFindByAccount() {
		Account acct = this.accountRepository.findById(accountId).get();
		List<Transaction> transactions = this.transactionRepository.findByAccount(acct);
		boolean nonZero = transactions.size() > 0;
		assert(nonZero);
	}

}
