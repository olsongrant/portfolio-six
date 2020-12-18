package com.formulafund.portfolio.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.formulafund.portfolio.data", "com.formulafund.portfolio.web"})
public class MwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MwebApplication.class, args);
	}

}
