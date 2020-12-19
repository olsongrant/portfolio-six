package com.formulafund.portfolio.data.model;

import java.time.LocalDateTime;

public class Transaction extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private Ticker ticker;
	private Float shareQuantity;
	private LocalDateTime transactionDateTime;
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
	public LocalDateTime getTransactionDateTime() {
		return transactionDateTime;
	}
	public void setTransactionDateTime(LocalDateTime transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}
	public Portfolio getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	private Portfolio portfolio;
	private TransactionType transactionType;
	
	public static Transaction purchaseOf(Ticker aTicker, LocalDateTime aPurchaseDate, Portfolio aPortfolio, Float quantity) {
		Transaction p = untypedTransactionWith(aTicker, aPurchaseDate, aPortfolio, quantity);
		p.setTransactionType(TransactionType.PURCHASE);
		return p;
	}
	
	public static Transaction saleOf(Ticker aTicker, LocalDateTime aSaleDate, Portfolio aPortfolio, Float quantity) {
		Transaction s = Transaction.untypedTransactionWith(aTicker, aSaleDate, aPortfolio, quantity);
		s.setTransactionType(TransactionType.SALE);
		return s;
	}
	@Override
	public String toString() {
		return "Transaction [ticker=" + ticker + ", shareQuantity=" + shareQuantity + ", transactionDateTime="
				+ transactionDateTime + ", portfolio=" + portfolio + ", transactionType=" + transactionType + ", id="
				+ id + "]";
	}
	public static Transaction untypedTransactionWith(Ticker aTicker, LocalDateTime aPurchaseDate, Portfolio aPortfolio,
			Float quantity) {
		Transaction p = new Transaction();
		p.setTicker(aTicker);
		p.setTransactionDateTime(aPurchaseDate);
		p.setPortfolio(aPortfolio);
		p.setShareQuantity(quantity);
		return p;
	}

}
