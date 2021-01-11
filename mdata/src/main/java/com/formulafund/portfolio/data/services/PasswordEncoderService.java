package com.formulafund.portfolio.data.services;

public interface PasswordEncoderService {

	String encode(String password);
	
	boolean matches(String rawPassword, String encodedPassword);

}
