package com.formulafund.portfolio.data.services;

import java.util.Optional;
import java.util.Set;

import com.formulafund.portfolio.data.model.Portfolio;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.StockPurchase;
import com.formulafund.portfolio.data.model.StockSale;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.view.HoldingView;

public interface PortfolioService extends CrudService<Portfolio> {
	
	Float getCurrentHoldingOf(Ticker aTicker, Portfolio aPortfolio);
	Set<StockHolding> getCurrentHoldings(Portfolio aPortfolio);
	Set<StockPurchase> allPurchases();
	Set<StockPurchase> allPurchasesForPortfolio(Portfolio aPortfolio);
	Set<StockSale> allSales();
	Set<StockSale> allSalesForPortfolio(Portfolio aPortfolio);
	Float sellAndReportRemaining(Ticker aTicker, Float quantity, Portfolio aPortfolio);
	Set<HoldingView> getHoldingsView(String portfolioName);
	Optional<Portfolio> findPortfolioByName(String aName);
}
