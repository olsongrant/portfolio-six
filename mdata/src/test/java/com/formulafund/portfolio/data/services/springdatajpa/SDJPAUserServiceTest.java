package com.formulafund.portfolio.data.services.springdatajpa;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class SDJPAUserServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	SDJPAUserService userService;
	
	HashMap<Long, ApplicationUser> tracker = new HashMap<>();

	@BeforeEach
	void setUp() throws Exception {
		ApplicationUser guy = ApplicationUser.with("Guy", "Smiley", "guy");
		ApplicationUser janie = ApplicationUser.with("Janie", "Lane", "janie");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFindAll() {
		Set<ApplicationUser> foundUsers = this.userService.findAll();
		verify(this.userRepository).findAll();
	}

	@Test
	void testFindById() {
		Long anId = 10L;
//		assertEquals(anId, aUser.getId());
		ApplicationUser u = this.userService.findById(anId);
		verify(this.userRepository).findById(anyLong());
	}

	@Test
	void testSave() {
		ApplicationUser bugs = ApplicationUser.with("Bugs", "Bunny", "bugs");
		this.userService.save(bugs);
		this.tracker.put(bugs.getId(), bugs);
		verify(this.userRepository).save(any());
	}

	@Test
	void testDelete() {
		int previousSize = this.userService.findAll().size();
		ApplicationUser aUser = ApplicationUser.with("ToBe", "Deleted", "tobe");
		this.userService.delete(aUser);
		int afterDeleteSize = this.userService.findAll().size();
		verify(this.userRepository).delete(any());
	}

	@Test
	void testDeleteById() {
		Long anId = 10L;
		this.userService.deleteById(anId);
		verify(this.userRepository).deleteById(anyLong());
	}

}
