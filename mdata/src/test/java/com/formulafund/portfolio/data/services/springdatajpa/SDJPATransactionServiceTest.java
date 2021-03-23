package com.formulafund.portfolio.data.services.springdatajpa;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.repositories.TransactionRepository;


class SDJPATransactionServiceTest {
	
	@Mock
	TransactionRepository transactionRepository;
	
	@Autowired
	SDJPATransactionService transactionService;
	
	
	List<Transaction> makeTransactionList() {
		ArrayList<Transaction> list = new ArrayList<>();
		List<String> tickers = List.of("QQQ", "GBTC", "GLD");
		AtomicLong nextId = new AtomicLong(0L);
		for (String tickString: tickers) {
			Transaction t = new Transaction();
			t.setId(nextId.getAndIncrement());
			Ticker ticker = new Ticker();
			ticker.setSymbol(tickString);
			t.setTicker(ticker);
			list.add(t);
		}
		return list;
	}

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.transactionService = new SDJPATransactionService(this.transactionRepository);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testAllTransactionsForAccount() {
		Account acct = SDJPATransactionServiceTest.makeCaptainKirkUniversalAccount();
		when( 
				this.transactionRepository.findByAccount(acct))
				.thenReturn(this.makeTransactionList());
		Set<Transaction> txnSet = this.transactionService.allTransactionsForAccount(acct);
		assert(txnSet.size() > 1);
	}
	


	private static ApplicationUser makeCaptainKirkUser() {
        ApplicationUser kirkUser = ApplicationUser.with("James", "Kirk", "captainkirk");
        kirkUser.setEmailAddress("kirk@enterprise.com");
        kirkUser.setId(2L);
        return kirkUser;
	}
	
	private static Account makeCaptainKirkUniversalAccount() {
		Account account = Account.with("universal", SDJPATransactionServiceTest.makeCaptainKirkUser());
		account.setId(99L);
		return account;
	}

}
