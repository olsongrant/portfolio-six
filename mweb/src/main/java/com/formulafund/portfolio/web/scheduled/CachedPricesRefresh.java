package com.formulafund.portfolio.web.scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formulafund.portfolio.data.model.CachedPrice;
import com.formulafund.portfolio.data.services.CachedPriceService;
import com.formulafund.portfolio.data.services.freestuff.BigChartsQuoteService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CachedPricesRefresh {
	
	private CachedPriceService cachedPriceService;
	private BigChartsQuoteService quoteService;
	
	protected static List<String> tickers = List.of("GLD", "GBTC", "EMQQ", "QQQ", "KBA",
													"XBI", "SPY", "IWN", "IJT", "GVAL");
	
	public CachedPricesRefresh(CachedPriceService aCachedPriceService,
							   BigChartsQuoteService aQuoteService) {
		this.cachedPriceService = aCachedPriceService;
		this.quoteService = aQuoteService;
	}
	
	@Transactional
	@Scheduled(cron = "10 0/3 * * * *")
	public void execute() {
		log.info("CachedPriceRefresh::execute");
		Map<String, Float> prices = this.quoteService.pricesForSymbols(tickers);
		for(Map.Entry<String, Float> tuple: prices.entrySet()) {
			Optional<CachedPrice> potentialPrice = this.cachedPriceService.findBySymbol(tuple.getKey());
			CachedPrice price;
			if (potentialPrice.isPresent()) {
				price = potentialPrice.get();
			} else {
				price = new CachedPrice();
				price.setTickerSymbol(tuple.getKey());
			}		
			price.setLatestPrice(tuple.getValue());
			price.setTimestamp(LocalDateTime.now());
			this.cachedPriceService.save(price);
		}
		log.info("leaving CachedPriceRefresh::execute");
	}

}
