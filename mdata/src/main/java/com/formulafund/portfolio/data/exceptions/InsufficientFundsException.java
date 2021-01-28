package com.formulafund.portfolio.data.exceptions;

public class InsufficientFundsException extends RuntimeException {
	public InsufficientFundsException(String aMessage) {
		super(aMessage);
	}

}
