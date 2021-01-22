package com.formulafund.portfolio.data.services.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.BCryptEncoderService;
import com.formulafund.portfolio.data.services.PasswordResetTokenService;
import com.formulafund.portfolio.data.services.UserService;

class MapUserServiceTest {
	
	UserService userService;
	PasswordResetTokenService passwordResetTokenService;
	HashMap<Long, ApplicationUser> tracker = new HashMap<>();

	@BeforeEach
	void setUp() throws Exception {
		
		BCryptEncoderService encoderService = new BCryptEncoderService();
		this.passwordResetTokenService = new MapPasswordResetTokenService();
		this.userService = new MapUserService(encoderService, this.passwordResetTokenService);
		ApplicationUser guy = ApplicationUser.with("Guy", "Smiley", "guy");
		ApplicationUser janie = ApplicationUser.with("Janie", "Lane", "janie");
		this.userService.save(guy);
		this.userService.save(janie);
		this.tracker.put(guy.getId(), guy);
		this.tracker.put(janie.getId(), janie);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFindAll() {
		Set<ApplicationUser> foundUsers = this.userService.findAll();
		assertEquals(this.tracker.keySet().size(), foundUsers.size());
	}

	@Test
	void testFindById() {
		Long anId = this.tracker.keySet().stream().findAny().get();
		ApplicationUser aUser = this.userService.findById(anId);
		assertEquals(anId, aUser.getId());
	}

	@Test
	void testSave() {
		ApplicationUser bugs = ApplicationUser.with("Bugs", "Bunny", "bugs");
		this.userService.save(bugs);
		assertNotNull(bugs.getId());
		this.tracker.put(bugs.getId(), bugs);
		
	}

	@Test
	void testDeleteById() {
		int previousSize = this.userService.findAll().size();
		Long anId = this.tracker.keySet().stream().findAny().get();
		this.userService.deleteById(anId);
		int afterDeleteSize = this.userService.findAll().size();
		assertNotEquals(previousSize, afterDeleteSize);
	}

	@Test
	void testDelete() {
		int previousSize = this.userService.findAll().size();
		ApplicationUser aUser = this.tracker.values().stream().findAny().get();
		this.userService.delete(aUser);
		int afterDeleteSize = this.userService.findAll().size();
		assertNotEquals(previousSize, afterDeleteSize);
	}

}
