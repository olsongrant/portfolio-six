package com.formulafund.portfolio.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ComponentScan({"com.formulafund.portfolio.data", "com.formulafund.portfolio.web"})
@Profile("map")
class MwebApplicationTests {

	@Test
	void contextLoads() {
	}

}
