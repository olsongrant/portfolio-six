package com.formulafund.portfolio.data.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StockPurchase extends BaseEntity implements Transaction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Ticker ticker;
	private Float shareQuantity;
	private LocalDateTime purchaseDateTime;
	public LocalDateTime getPurchaseDateTime() {
		return purchaseDateTime;
	}
	public void setPurchaseDateTime(LocalDateTime purchaseDateTime) {
		this.purchaseDateTime = purchaseDateTime;
	}
	private Portfolio portfolio;
	
	
	public Portfolio getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public Ticker getTicker() {
		return ticker;
	}
	public void setTicker(Ticker ticker) {
		this.ticker = ticker;
	}
	public Float getShareQuantity() {
		return shareQuantity;
	}
	public void setShareQuantity(Float shareQuantity) {
		this.shareQuantity = shareQuantity;
	}
	
	@Override
	public String toString() {
		return "StockPurchase [ticker=" + ticker + ", shareQuantity=" + shareQuantity + ", purchaseDateTime=" + purchaseDateTime
				+ ", portfolio=" + portfolio + ", id=" + id + "]";
	}
	public static StockPurchase of(Ticker aTicker, LocalDateTime aPurchaseDate, Portfolio aPortfolio, Float quantity) {
		StockPurchase p = new StockPurchase();
		p.setTicker(aTicker);
		p.setPurchaseDateTime(aPurchaseDate);
		p.setPortfolio(aPortfolio);
		p.setShareQuantity(quantity);
		return p;
	}
	@Override
	public LocalDateTime getTransactionDateTime() {
		return this.getPurchaseDateTime();
	}
	@Override
	public TransactionType getTransactionType() {
		return TransactionType.PURCHASE;
	}
}
