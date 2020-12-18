package com.formulafund.portfolio.data.model;

import java.time.LocalDateTime;

public interface Transaction {
	public LocalDateTime getTransactionDateTime();
	public Float getShareQuantity();
	public void setShareQuantity(Float aQuantity);
	public Ticker getTicker();
	public void setTicker(Ticker aTicker);
	public TransactionType getTransactionType();
	public Portfolio getPortfolio();
}
