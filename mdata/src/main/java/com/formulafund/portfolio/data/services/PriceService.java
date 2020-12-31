package com.formulafund.portfolio.data.services;

import com.formulafund.portfolio.data.model.Ticker;

public interface PriceService {
	
	Float sharePriceForTicker(Ticker aTicker);
	
	public default Float balanceAfterDeduction(Float shareQuantity, Float currentCash, Float sharePrice) {
    	Float deduction = shareQuantity * sharePrice;
    	Float unroundedNewCurrent = currentCash - deduction;
    	Float roundedNewCurrent = Math.round(unroundedNewCurrent * 100.0f) / 100.0f;
		return roundedNewCurrent;
	}
	
	public default Float balanceAfterCredit(Float shareQuantity, Float currentCash, Float sharePrice) {
    	Float credit = shareQuantity * sharePrice;
    	Float unroundedNewCurrent = currentCash + credit;
    	Float roundedNewCurrent = Math.round(unroundedNewCurrent * 100.0f) / 100.0f;
		return roundedNewCurrent;
	}

}
