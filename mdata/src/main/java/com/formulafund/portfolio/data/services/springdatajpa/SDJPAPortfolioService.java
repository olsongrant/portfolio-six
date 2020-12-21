package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Portfolio;
import com.formulafund.portfolio.data.model.StockHolding;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.repositories.PortfolioRepository;
import com.formulafund.portfolio.data.services.PortfolioService;
import com.formulafund.portfolio.data.services.TransactionService;
import com.formulafund.portfolio.data.view.HoldingView;

@Service
@Profile("springdatajpa")
public class SDJPAPortfolioService implements PortfolioService {
	
	private PortfolioRepository portfolioRepository;
	private TransactionService transactionService;
	
	public SDJPAPortfolioService(PortfolioRepository pRepository, TransactionService tService) {
		this.portfolioRepository = pRepository;
		this.transactionService = tService;
	}

	@Override
	public Set<Portfolio> findAll() {
//		Set<Portfolio> portfolios = new HashSet<>();
//		Stream<Portfolio> pStream = StreamSupport.stream(this.portfolioRepository.findAll().spliterator(), false);
//		portfolios = pStream.collect(Collectors.toSet());
//		return portfolios;
		return StreamSupport.stream(this.portfolioRepository.findAll()
				.spliterator(), false).collect(Collectors.toSet());
	}

	@Override
	public Portfolio findById(Long id) {
		return this.portfolioRepository.findById(id).orElse(null);
	}

	@Override
	public Portfolio save(Portfolio object) {
		return this.portfolioRepository.save(object);
	}

	@Override
	public void delete(Portfolio object) {
		this.portfolioRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		this.portfolioRepository.deleteById(id);
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
