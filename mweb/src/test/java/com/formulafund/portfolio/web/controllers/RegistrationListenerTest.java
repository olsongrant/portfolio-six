package com.formulafund.portfolio.web.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


//@SpringBootTest(classes = TestConfig.class)
class RegistrationListenerTest {
	
	@Autowired
	RegistrationListener registrationListener;

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	//@Test
	void testSendTestEmail() {
		registrationListener.sendTestEmail();
	}

}
