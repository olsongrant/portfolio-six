package com.formulafund.portfolio.data.services;

import java.util.Set;

import com.formulafund.portfolio.data.model.Portfolio;
import com.formulafund.portfolio.data.model.StockPurchase;

public interface StockPurchaseService extends CrudService<StockPurchase> {

	public Set<StockPurchase> purchasesForPortfolio(Portfolio aPortfolio); 

}
