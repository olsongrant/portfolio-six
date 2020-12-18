package com.formulafund.portfolio.data.model;

public class Ticker extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IssuingCompany issuingCompany;
	private String symbol;
	private Exchange exchange;
	public Exchange getExchange() {
		return exchange;
	}
	public void setExchange(Exchange exchange) {
		this.exchange = exchange;
	}
	public IssuingCompany getIssuingCompany() {
		return issuingCompany;
	}
	public void setIssuingCompany(IssuingCompany issuingCompany) {
		this.issuingCompany = issuingCompany;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	@Override
	public String toString() {
		return "Ticker [issuingCompany=" + issuingCompany + ", symbol=" + symbol + ", exchange=" + exchange + ", id="
				+ id + "]";
	}
}
