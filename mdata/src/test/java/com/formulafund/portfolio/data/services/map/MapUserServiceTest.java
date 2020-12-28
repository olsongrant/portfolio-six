package com.formulafund.portfolio.data.services.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.formulafund.portfolio.data.model.User;
import com.formulafund.portfolio.data.services.UserService;

class MapUserServiceTest {
	
	UserService userService;
	HashMap<Long, User> tracker = new HashMap<>();

	@BeforeEach
	void setUp() throws Exception {
		this.userService = new MapUserService();
		User guy = User.with("Guy Smiley", "guy");
		User janie = User.with("Janie Lane", "janie");
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
		Set<User> foundUsers = this.userService.findAll();
		assertEquals(this.tracker.keySet().size(), foundUsers.size());
	}

	@Test
	void testFindById() {
		Long anId = this.tracker.keySet().stream().findAny().get();
		User aUser = this.userService.findById(anId);
		assertEquals(anId, aUser.getId());
	}

	@Test
	void testSave() {
		User bugs = User.with("Bugs Bunny", "bugs");
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
		User aUser = this.tracker.values().stream().findAny().get();
		this.userService.delete(aUser);
		int afterDeleteSize = this.userService.findAll().size();
		assertNotEquals(previousSize, afterDeleteSize);
	}

}
