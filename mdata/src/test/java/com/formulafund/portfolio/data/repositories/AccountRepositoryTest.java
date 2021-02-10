package com.formulafund.portfolio.data.repositories;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;

import java.util.List;

@DataJpaTest
class AccountRepositoryTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	Long emailUserId;
	Long accountId;

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
	}

	@AfterEach
	void tearDown() throws Exception {
		this.accountRepository.deleteById(accountId);
		this.userRepository.deleteById(emailUserId);
	}

	@Test
	void testFindByName() {
		List<Account> accounts = this.accountRepository.findByName("fasttrade");
		boolean nonZero = accounts.size() > 0;
		assert(nonZero);
	}

}
