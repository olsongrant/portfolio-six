package com.formulafund.portfolio.data.services.freestuff;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.formulafund.portfolio.data.model.Exchange;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.PriceService;
import com.formulafund.portfolio.data.services.map.MapCachedPriceService;

@ExtendWith(MockitoExtension.class)
class BigChartsQuoteServiceTest {
	
	Ticker ticker = new Ticker();
	
	BigChartsQuoteService priceService = new BigChartsQuoteService(new MapCachedPriceService());

	@BeforeEach
	void setUp() throws Exception {
		ticker.setSymbol("MSFT");
		ticker.setExchange(Exchange.NASDAQ);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSharePriceForTicker() {
		assertNotNull(ticker);
		ticker.setSymbol("MSFT");
		Float msftPrice = this.priceService.sharePriceForTicker(ticker);
		assertNotNull(msftPrice);
	}

	@Test
	void testSharePriceForSymbol() {
		Float amazonPrice = this.priceService.sharePriceForSymbol("AMZN");
	}

}
