package com.formulafund.portfolio.data.services.map;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.Portfolio;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.TransactionType;
import com.formulafund.portfolio.data.model.User;
import com.formulafund.portfolio.data.services.InadequatePositionException;
import com.formulafund.portfolio.data.services.PortfolioService;
import com.formulafund.portfolio.data.services.TransactionService;
import com.formulafund.portfolio.data.view.HoldingView;

@Service
@Profile("map")
public class MapPortfolioService extends BaseMapService<Portfolio> implements PortfolioService {

	private TransactionService transactionService;

	
	public MapPortfolioService(TransactionService tService) {
		this.transactionService = tService;
	}


	@Override
	public Float getCurrentHoldingOf(Ticker aTicker, Portfolio aPortfolio) {
		return PortfolioService.getCurrentHoldingOf(this.transactionService, aTicker, aPortfolio);
	}


	@Override
	public Set<StockHolding> getCurrentHoldings(Portfolio aPortfolio) {	
		return this.getCurrentHoldings(this.transactionService, aPortfolio);
	}


	@Override
	public Float sellAndReportRemaining(Ticker aTicker, Float quantity, Portfolio aPortfolio) {
		return this.sellAndReportRemaining(this.transactionService, aTicker, quantity, aPortfolio);
	}


}
