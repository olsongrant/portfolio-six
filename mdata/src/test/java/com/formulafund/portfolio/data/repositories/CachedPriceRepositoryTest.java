package com.formulafund.portfolio.data.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.formulafund.portfolio.data.model.CachedPrice;

@DataJpaTest
class CachedPriceRepositoryTest {
	
	@Autowired
	CachedPriceRepository cachedPriceRepository;
	
	Long cachedPriceId;

	@BeforeEach
	void setUp() throws Exception {
		CachedPrice price = new CachedPrice();
		price.setLatestPrice(9.99f);
		price.setTimestamp(LocalDateTime.now());
		price.setTickerSymbol("ZZTOP");
		price = this.cachedPriceRepository.save(price);
		this.cachedPriceId = price.getId();
	}

	@AfterEach
	void tearDown() throws Exception {
		this.cachedPriceRepository.deleteById(cachedPriceId);
	}

	@Test
	void testFindByTickerSymbol() {
		List<CachedPrice> prices = this.cachedPriceRepository.findByTickerSymbol("ZZTOP");
		boolean nonZero = prices.size() > 0;
		assert(nonZero);
	}

}
