package com.formulafund.portfolio.data.exceptions;

public class TickerNotFoundException extends RuntimeException {
	public TickerNotFoundException(String aMessage) {
		super(aMessage);
	}
}
