package com.formulafund.portfolio.data.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


@lombok.extern.slf4j.Slf4j
class BCryptEncoderServiceTest {
	
	BCryptEncoderService encoder = new BCryptEncoderService();
	String alreadyEncodedPassword;

	@BeforeEach
	void setUp() throws Exception {
		this.alreadyEncodedPassword = encoder.encode("myRawPassword");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testEncode() {
		String raw = "myRawPassword";
		String encoded = this.encoder.encode(raw);
		log.info("raw password: " + raw + ". encoded: " + encoded);
		assertNotEquals(raw, encoded);
	}

	@Test
	void testMatches() {
		String raw = "myRawPassword";
		boolean matches = this.encoder.matches(raw, alreadyEncodedPassword);
		log.info("returned from matches(): " + matches);
		Assertions.assertTrue(matches);
	}

}
