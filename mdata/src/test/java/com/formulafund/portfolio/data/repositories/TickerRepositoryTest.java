package com.formulafund.portfolio.data.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.formulafund.portfolio.data.model.Exchange;
import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.Ticker;

@DataJpaTest
class TickerRepositoryTest {
	
	@Autowired
	IssuingCompanyRepository icRepository;
	
	@Autowired
	TickerRepository tickerRepository;
	
	Long issuingCompanyId;
	Long tickerId;

	@BeforeEach
	void setUp() throws Exception {
		IssuingCompany issuingCompany = new IssuingCompany();
		issuingCompany.setFullName("Acme Rocket Launchers");
		issuingCompany = this.icRepository.save(issuingCompany);
		this.issuingCompanyId = issuingCompany.getId();
		Ticker ticker = new Ticker();
		ticker.setExchange(Exchange.OTC);
		ticker.setIssuingCompany(issuingCompany);
		ticker.setSymbol("ACMERL");
		ticker = this.tickerRepository.save(ticker);
		this.tickerId = ticker.getId();
	}

	@AfterEach
	void tearDown() throws Exception {
		this.tickerRepository.deleteById(tickerId);
		this.icRepository.deleteById(issuingCompanyId);
	}

	@Test
	void testFindBySymbol() {
		List<Ticker> tickers = this.tickerRepository.findBySymbol("ACMERL");
		boolean nonZero = tickers.size() > 0;
		assert(nonZero);
	}

}
