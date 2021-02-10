package com.formulafund.portfolio.data.services;

import java.util.Optional;

import com.formulafund.portfolio.data.model.Exchange;
import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.Ticker;

public interface TickerService extends CrudService<Ticker> {
	
	public default Optional<Ticker> findTickerBySymbol(String aSymbol) {
		return Optional.ofNullable(this.findBySymbol(aSymbol));
	}
	
	public default Ticker findBySymbol(String aSymbol) {
		Optional<Ticker> potentialTicker = this.findAll()
		.stream()
		.filter(t -> aSymbol.equalsIgnoreCase(t.getSymbol()))
		.findAny();
		if (potentialTicker.isPresent()) {
			return potentialTicker.get();
		} else {
			return null;
		}
	}

	public default Ticker getInstanceFor(String targetTicker, IssuingCompany co, Exchange ex) {
		Optional<Ticker> potentialTicker = this.findTickerBySymbol(targetTicker);
		if (potentialTicker.isPresent()) {
			return potentialTicker.get();
		} else {
			Ticker t = new Ticker();
			t.setSymbol(targetTicker);
			t.setIssuingCompany(co);
			t.setExchange(ex);
			this.save(t);
			return t;
		}
	}

}
