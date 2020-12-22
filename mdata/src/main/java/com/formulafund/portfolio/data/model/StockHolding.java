package com.formulafund.portfolio.data.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class StockHolding {

	private Ticker ticker;
	private Float shareQuantity;
	public StockHolding(Ticker aTicker, Float numberOfShares) {
		this.ticker = aTicker;
		this.shareQuantity = numberOfShares;
	}

	@Override
	public String toString() {
		return "StockHolding [ticker=" + ticker + ", shareQuantity=" + shareQuantity + "]";
	}

}
