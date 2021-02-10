package com.formulafund.portfolio.data.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.PasswordResetToken;

@DataJpaTest
class PasswordResetTokenRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordResetTokenRepository passwordTokenRepository;
	
	Long emailUserId;	
	Long pwdTokenId;
	
	@BeforeEach
	void setUp() throws Exception {
		ApplicationUser emailUser = ApplicationUser.with("Joey", "Bagadonuts", "bagadonuts");
		emailUser.setEmailAddress("joey.b@address.com");
		emailUser.setEnabled(true);
		emailUser = this.userRepository.save(emailUser);
		this.emailUserId = emailUser.getId();
		
		PasswordResetToken pwdToken = new PasswordResetToken();
		pwdToken.setToken("8675309");
		LocalDateTime yesterday = LocalDateTime.now().minusDays(1L);
		pwdToken.setExpiryDate(yesterday);
		pwdToken.setUser(emailUser);
		pwdToken = this.passwordTokenRepository.save(pwdToken);
		this.pwdTokenId = pwdToken.getId();
	}

	@AfterEach
	void tearDown() throws Exception {
		this.userRepository.deleteById(emailUserId);		
	}

	@Test
	void testFindByToken() {
		List<PasswordResetToken> tokenList = this.passwordTokenRepository.findByToken("8675309");
		boolean nonZero = tokenList.size() > 0;
		assert(nonZero);
		PasswordResetToken token = tokenList.get(0);
		assertEquals("8675309", token.getToken());
	}

	@Test
	void testFindByApplicationUser() {
		Optional<ApplicationUser> potentialUser = this.userRepository.findById(emailUserId);
		PasswordResetToken token = this.passwordTokenRepository.findByUser(potentialUser.get()).get(0);
		assertNotNull(token);
	}

	@Test
	void testFindAllByExpiryDateLessThan() {
		List<PasswordResetToken> tokens = 
				this.passwordTokenRepository.findAllByExpiryDateLessThan(LocalDateTime.now());
		boolean nonZero = tokens.size() > 0;
		assert(nonZero);
	}



}
