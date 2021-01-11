package com.formulafund.portfolio.data.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Getter
@Setter
@Slf4j
public class BCryptEncoderService implements PasswordEncoderService {

	private BCryptPasswordEncoder bcryptEncoder;
	
	public BCryptEncoderService() {
		log.info("BCryptEncoderService::constructor invoked");
		this.bcryptEncoder = new BCryptPasswordEncoder();
	}
	
	@Override
	public String encode(String password) {
		String encoded = this.bcryptEncoder.encode(password);
		return encoded;
	}

	@Override
	public boolean matches(String aRawPassword, String anEncodedPassword) {
		log.debug("BCryptEncoderService::matches(" + aRawPassword + ", " + anEncodedPassword + ")");
		boolean matches = this.bcryptEncoder.matches(aRawPassword, anEncodedPassword);
		log.debug("result of BCryptEncoderService::matches: " + matches);
		return matches;
	}
}
