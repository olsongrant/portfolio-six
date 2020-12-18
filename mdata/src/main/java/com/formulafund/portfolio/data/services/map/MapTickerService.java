package com.formulafund.portfolio.data.services.map;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Exchange;
import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.IssuingCompanyService;
import com.formulafund.portfolio.data.services.TickerService;


@Service
public class MapTickerService extends BaseMapService<Ticker> implements TickerService {
	private IssuingCompanyService issuingCompanyService;
	
	public MapTickerService(IssuingCompanyService ics) {
		this.issuingCompanyService = ics;
	}
	
	

	@Override
	public Ticker save(Ticker t) {
		/*
		 * I think we may want to let Ticker entities be created without an IssuingCompany,
		 * and then let a cron job fill in the company later.
		 */
		IssuingCompany company = t.getIssuingCompany();
		if ((company != null) && (!company.hasId())) {
			this.issuingCompanyService.save(company);
		}
		return super.save(t);
	}



	@Override
	public Ticker getInstanceFor(String targetTicker, IssuingCompany co, Exchange ex) {
		Optional<Ticker> potentialTicker = this.map.values()
				.stream()
				.filter(t -> targetTicker.equals(t.getSymbol()))
				.findAny();
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
