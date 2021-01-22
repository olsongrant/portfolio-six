package com.formulafund.portfolio.web.security;

import org.springframework.security.core.AuthenticationException;

public class WrongAuthenticationMechanismException extends AuthenticationException {

	public WrongAuthenticationMechanismException(String msg) {
		super(msg);
	}

}
