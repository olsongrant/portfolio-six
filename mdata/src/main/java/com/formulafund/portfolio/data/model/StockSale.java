package com.formulafund.portfolio.data.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StockSale extends BaseEntity implements Transaction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LocalDateTime saleDateTime;

	public LocalDateTime getSaleDateTime() {
		return saleDateTime;
	}
	public void setSaleDateTime(LocalDateTime saleDateTime) {
		this.saleDateTime = saleDateTime;
	}
	public Ticker getTicker() {
		return ticker;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}
	public Float getSharesSold() {
		return sharesSold;
	}
	public void setSharesSold(Float sharesSold) {
		this.sharesSold = sharesSold;
	}
	private Ticker ticker;
	private Portfolio portfolio;
	private Float sharesSold;
	@Override
	public String toString() {
		return "StockSale [saleDateTime=" + saleDateTime + ", ticker=" + ticker + ", portfolio=" + portfolio + ", sharesSold="
				+ sharesSold + ", id=" + id + "]";
	}
	
	public static StockSale of(Ticker aTicker, LocalDateTime aSaleDate, Portfolio aPortfolio, Float quantity) {
		StockSale p = new StockSale();
		p.setTicker(aTicker);
		p.setSaleDateTime(aSaleDate);
		p.setPortfolio(aPortfolio);
		p.setSharesSold(quantity);
		return p;
	}
	@Override
	public LocalDateTime getTransactionDateTime() {
		return this.getSaleDateTime();
	}
	@Override
	public Float getShareQuantity() {
		return this.getSharesSold();
	}
	@Override
	public void setShareQuantity(Float aQuantity) {
		this.setSharesSold(aQuantity);	
	}
	@Override
	public void setTicker(Ticker aTicker) {
		this.ticker = aTicker;
	}
	@Override
	public TransactionType getTransactionType() {
		return TransactionType.SALE;
	}
}
