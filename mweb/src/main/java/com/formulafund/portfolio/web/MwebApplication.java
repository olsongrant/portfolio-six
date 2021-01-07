package com.formulafund.portfolio.web;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
@ComponentScan({"com.formulafund.portfolio.data", "com.formulafund.portfolio.web"})
public class MwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MwebApplication.class, args);
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages");
		String labelFormTitle = messageSource.getMessage("label.form.title", null, Locale.ENGLISH);
		System.out.println("label.form.title (a verification of the resource bundle availability): " + labelFormTitle);

	}

}
