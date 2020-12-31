package com.formulafund.portfolio.data.services;

public class InsufficientFundsException extends RuntimeException {
	public InsufficientFundsException(String aMessage) {
		super(aMessage);
	}

}
