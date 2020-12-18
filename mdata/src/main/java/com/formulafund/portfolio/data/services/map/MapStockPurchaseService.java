package com.formulafund.portfolio.data.services.map;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Portfolio;
import com.formulafund.portfolio.data.model.StockPurchase;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.PortfolioService;
import com.formulafund.portfolio.data.services.TickerService;

@Service
public class MapStockPurchaseService extends BaseMapService<StockPurchase>
		implements com.formulafund.portfolio.data.services.StockPurchaseService {
	
	private TickerService tickerService;
	
	public MapStockPurchaseService(TickerService t) {
		this.tickerService = t;
	}

	@Override
	public StockPurchase save(StockPurchase sp) {
		// make sure Ticker is saved
		// make sure Portfolio is saved
		Ticker ticker = sp.getTicker();
		if (!ticker.hasId()) {
			this.tickerService.save(ticker);
		}
		return super.save(sp);
	}

	public Set<StockPurchase> purchasesForPortfolio(Portfolio aPortfolio) {
		Set<StockPurchase> purchases = this.map.values()
			.stream()
			.filter(sp -> aPortfolio.equals(sp.getPortfolio()))
			.collect(Collectors.toSet());
		return purchases;
	}
}
