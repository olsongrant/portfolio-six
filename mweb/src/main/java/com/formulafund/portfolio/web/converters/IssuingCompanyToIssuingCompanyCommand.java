package com.formulafund.portfolio.web.converters;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.*;
import com.formulafund.portfolio.web.commands.*;
import java.util.*;


import lombok.Synchronized;

@Component
public class IssuingCompanyToIssuingCompanyCommand implements Converter<IssuingCompany, IssuingCompanyCommand> {

	private TickerToTickerCommand tickerConverter;
	
	public IssuingCompanyToIssuingCompanyCommand(@Lazy TickerToTickerCommand tConverter) {
		this.tickerConverter = tConverter;
	}
	
	@Synchronized
	@Nullable
	@Override
	public IssuingCompanyCommand convert(IssuingCompany source) {
		if (source == null) return null;
		IssuingCompanyCommand icCommand = new IssuingCompanyCommand();
		icCommand.setFullName(source.getFullName());
		icCommand.setId(source.getId());
		Set<Ticker> tickers = source.getTickers();
		if ((tickers != null) && (!tickers.isEmpty())) {
			HashSet<TickerCommand> cmds = new HashSet<>();
			tickers.forEach(t -> cmds.add(this.tickerConverter.convert(t)));
			icCommand.setTickerCommands(cmds);
		}
		return icCommand;
	}

}
