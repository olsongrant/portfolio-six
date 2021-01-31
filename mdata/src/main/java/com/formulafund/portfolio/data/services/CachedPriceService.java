package com.formulafund.portfolio.data.services;

import java.util.Optional;

import com.formulafund.portfolio.data.model.CachedPrice;

public interface CachedPriceService extends CrudService<CachedPrice> {
	
	public default Optional<CachedPrice> findBySymbol(String aSymbol) {
		if (aSymbol == null) throw new RuntimeException("sent in null to CachedPriceService::findBySymbol");
		return this.findAll()
				.stream()
				.filter(c -> aSymbol.equalsIgnoreCase(c.getTickerSymbol()))
				.findAny();
	}

}
