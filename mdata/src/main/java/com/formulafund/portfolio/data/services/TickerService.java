package com.formulafund.portfolio.data.services;

import com.formulafund.portfolio.data.model.Exchange;
import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.Ticker;

public interface TickerService extends CrudService<Ticker> {

	Ticker getInstanceFor(String symbol, IssuingCompany co, Exchange ex);

}
