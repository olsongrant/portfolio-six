package com.formulafund.portfolio.data.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.PasswordResetToken;
import com.formulafund.portfolio.data.model.VerificationToken;

@DataJpaTest
class VerificationTokenRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	VerificationTokenRepository verificationTokenRepository;
	
	Long emailUserId;	
	Long verificationTokenId;
	
	@BeforeEach
	void setUp() throws Exception {
		ApplicationUser emailUser = ApplicationUser.with("Joey", "Bagadonuts", "bagadonuts");
		emailUser.setEmailAddress("joey.b@address.com");
		emailUser.setEnabled(true);
		emailUser = this.userRepository.save(emailUser);
		this.emailUserId = emailUser.getId();
		
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken("8675309");
		LocalDateTime yesterday = LocalDateTime.now().minusDays(1L);
		verificationToken.setExpiryDate(yesterday);
		verificationToken.setUser(emailUser);
		verificationToken = this.verificationTokenRepository.save(verificationToken);
		this.verificationTokenId = verificationToken.getId();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFindByToken() {
		VerificationToken token = this.verificationTokenRepository.findByToken("8675309");
		boolean nonZero = token != null;
		assert(nonZero);
		assertEquals("8675309", token.getToken());
	}

	@Test
	void testFindByApplicationUser() {
		Optional<ApplicationUser> potentialUser = this.userRepository.findById(emailUserId);
		VerificationToken token = this.verificationTokenRepository.findByUser(potentialUser.get());
		assertNotNull(token);
	}

	@Test
	void testFindAllByExpiryDateLessThan() {
		List<VerificationToken> tokens = 
				this.verificationTokenRepository.findAllByExpiryDateLessThan(LocalDateTime.now());
		boolean nonZero = tokens.size() > 0;
		assert(nonZero);
	}

}
