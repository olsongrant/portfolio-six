package com.formulafund.portfolio.data.model;

public class StockHolding {

	private Ticker ticker;
	private Float shareQuantity;
	public StockHolding(Ticker aTicker, Float numberOfShares) {
		this.ticker = aTicker;
		this.shareQuantity = numberOfShares;
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
		return "StockHolding [ticker=" + ticker + ", shareQuantity=" + shareQuantity + "]";
	}

}
