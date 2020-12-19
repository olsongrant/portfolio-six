package com.formulafund.portfolio.data.services;

import java.util.Optional;
import java.util.Set;

import com.formulafund.portfolio.data.model.Portfolio;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.view.HoldingView;

public interface PortfolioService extends CrudService<Portfolio> {
	
	Float getCurrentHoldingOf(Ticker aTicker, Portfolio aPortfolio);
	Set<StockHolding> getCurrentHoldings(Portfolio aPortfolio);
	Set<Transaction> allPurchases();
	Set<Transaction> allPurchasesForPortfolio(Portfolio aPortfolio);
	Set<Transaction> allSales();
	Set<Transaction> allSalesForPortfolio(Portfolio aPortfolio);
	Float sellAndReportRemaining(Ticker aTicker, Float quantity, Portfolio aPortfolio);
	Set<HoldingView> getHoldingsView(String portfolioName);
	Optional<Portfolio> findPortfolioByName(String aName);
}
