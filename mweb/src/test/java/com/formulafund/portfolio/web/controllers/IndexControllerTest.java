package com.formulafund.portfolio.web.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.UserService;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {
	
    @Mock
    UserService userService;

    @InjectMocks
    IndexController controller;

    Set<ApplicationUser> users;

    MockMvc mockMvc;	

	@BeforeEach
	void setUp() throws Exception {
        this.users = new HashSet<>();
        ApplicationUser bugs = ApplicationUser.with("Bugs", "Bunny", "bugs");
 //       this.userService.save(bugs);
        this.users.add(bugs);
        ApplicationUser yosemite = ApplicationUser.with("Yosemite", "Sam", "sam");
        this.users.add(yosemite);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testUserAll() throws Exception {
        mockMvc.perform(get("/user/all"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attributeExists("userSet"));
	}

	@Test
	void testGetIndex() throws Exception {
        mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attributeExists("userSet"));        
    }

}
