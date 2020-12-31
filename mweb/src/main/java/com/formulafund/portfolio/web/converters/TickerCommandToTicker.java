package com.formulafund.portfolio.web.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.web.commands.TickerCommand;

import lombok.Synchronized;

@Component
public class TickerCommandToTicker implements Converter<TickerCommand, Ticker> {
	
	private IssuingCompanyCommandToIssuingCompany issuingCompanyCommandConverter;
	
	public TickerCommandToTicker(IssuingCompanyCommandToIssuingCompany iccConverter) {
		this.issuingCompanyCommandConverter = iccConverter;
	}

	@Synchronized
	@Nullable
	@Override
	public Ticker convert(TickerCommand source) {
		if (source == null) return null;
		Ticker ticker = new Ticker();
		ticker.setId(ticker.getId());
		ticker.setExchange(source.getExchange());
		ticker.setIssuingCompany(
				this.issuingCompanyCommandConverter.convert(
						source.getIssuingCompanyCommand()));
		ticker.setSymbol(source.getSymbol());
		return ticker;
	}

}
