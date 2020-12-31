package com.formulafund.portfolio.web.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.web.commands.TickerCommand;

import lombok.Synchronized;

@Component
public class TickerToTickerCommand implements Converter<Ticker, TickerCommand> {
	
	private IssuingCompanyToIssuingCompanyCommand issuingCompanyConverter;
	
	public TickerToTickerCommand(IssuingCompanyToIssuingCompanyCommand icConverter) {
		this.issuingCompanyConverter = icConverter;
	}

	@Synchronized
	@Nullable
	@Override
	public TickerCommand convert(Ticker source) {
		if (source == null) return null;
		TickerCommand cmd = new TickerCommand();
		cmd.setId(source.getId());
		cmd.setExchange(source.getExchange());
		cmd.setIssuingCompanyCommand(this.issuingCompanyConverter.convert(source.getIssuingCompany()));
		cmd.setSymbol(source.getSymbol());
		return cmd;
	}

}
