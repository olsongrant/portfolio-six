package com.formulafund.portfolio.web;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan({"com.formulafund.portfolio.data", "com.formulafund.portfolio.web"})
@EnableScheduling
@Slf4j
public class MwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MwebApplication.class, args);
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages");
		String labelFormTitle = messageSource.getMessage("label.form.title", null, Locale.ENGLISH);
		log.info("label.form.title (a verification of the resource bundle availability): " + labelFormTitle);

	}

}
