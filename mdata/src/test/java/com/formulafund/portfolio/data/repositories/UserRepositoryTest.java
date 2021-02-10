package com.formulafund.portfolio.data.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.formulafund.portfolio.data.model.ApplicationUser;

@DataJpaTest
class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;
	
	Long emailUserId;
	Long socialUserId;

	@BeforeEach
	void setUp() throws Exception {
		ApplicationUser emailUser = ApplicationUser.with("Joey", "Bagadonuts", "bagadonuts");
		emailUser.setEmailAddress("joey.b@address.com");
		emailUser.setEnabled(true);
		emailUser = this.userRepository.save(emailUser);
		this.emailUserId = emailUser.getId();
		ApplicationUser socialUser = ApplicationUser.with("Khloe", "Bagawind", "windbag");
		socialUser.setSocialPlatformId("8675309");
		socialUser.setEnabled(true);
		socialUser = this.userRepository.save(socialUser);
		this.socialUserId = socialUser.getId();
	}

	@AfterEach
	void tearDown() throws Exception {
		this.userRepository.deleteById(emailUserId);
		this.userRepository.deleteById(socialUserId);
	}

	@Test
	void testFindByEmailAddress() {
		List<ApplicationUser> userList = this.userRepository.findByEmailAddress("joey.b@address.com");
		boolean nonZero = userList.size() > 0;
		assert(nonZero);
		ApplicationUser joey = userList.get(0);
		assertEquals(this.emailUserId, joey.getId());
	}

	@Test
	void testFindBySocialPlatformId() {
		List<ApplicationUser> userList = this.userRepository.findBySocialPlatformId("8675309");
		boolean nonZero = userList.size() > 0;
		assert(nonZero);
		ApplicationUser khloe = userList.get(0);
		assertEquals(this.socialUserId, khloe.getId());
	}

}
