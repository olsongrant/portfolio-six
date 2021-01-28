package com.formulafund.portfolio.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(properties = { "spring.mail.password=pay_no_mind",
		"spring.security.oauth2.client.registration.facebook.client-id=pay_no_mind",
		"spring.security.oauth2.client.registration.facebook.client-secret=pay_no_mind"})
@ComponentScan({"com.formulafund.portfolio.data", "com.formulafund.portfolio.web"})
@Profile("map")
class MwebApplicationTests {

	@Test
	void contextLoads() {
	}

}
