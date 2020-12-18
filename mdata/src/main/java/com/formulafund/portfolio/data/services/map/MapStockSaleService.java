package com.formulafund.portfolio.data.services.map;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Portfolio;
import com.formulafund.portfolio.data.model.StockSale;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.TickerService;

@Service
public class MapStockSaleService extends BaseMapService<StockSale>
		implements com.formulafund.portfolio.data.services.StockSaleService {
	
	private TickerService tickerService;
	
	@Override
	public StockSale save(StockSale sale) {
		Ticker ticker = sale.getTicker();
		if (ticker == null) throw new RuntimeException("Ticker was null in MapStockSaleService::save");
		if (!ticker.hasId()) {
			this.tickerService.save(ticker);
		}
		if (sale.getPortfolio() == null) throw new RuntimeException("Portfolio was null in MapStockSaleService::save");
		if (!sale.getPortfolio().hasId()) throw new RuntimeException("Portfolio was not saved in MapStockSaleService::save");
		return super.save(sale);
	}

	public MapStockSaleService(TickerService t) {
		this.tickerService = t;
	}

	public Set<StockSale> salesForPortfolio(Portfolio aPortfolio) {
		Set<StockSale> sales = this.map.values()
			.stream()
			.filter(sp -> aPortfolio.equals(sp.getPortfolio()))
			.collect(Collectors.toSet());
		return sales;
	}



}
