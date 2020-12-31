package com.formulafund.portfolio.web.converters;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.web.commands.IssuingCompanyCommand;
import com.formulafund.portfolio.web.commands.*;

import java.util.*;

import lombok.Synchronized;

@Component
public class IssuingCompanyCommandToIssuingCompany implements Converter<IssuingCompanyCommand, IssuingCompany> {
	
	private TickerCommandToTicker tickerCommandConverter;
	
	public IssuingCompanyCommandToIssuingCompany(@Lazy TickerCommandToTicker tcConverter) {
		this.tickerCommandConverter = tcConverter;
	}
	
	@Synchronized
	@Nullable
	@Override
	public IssuingCompany convert(IssuingCompanyCommand source) {
		if (source == null) return null;
		IssuingCompany iCompany = new IssuingCompany();
		iCompany.setFullName(source.getFullName());
		iCompany.setId(source.getId());
		Set<TickerCommand> cmds = source.getTickerCommands();
		if ((cmds != null) && (!cmds.isEmpty())) {
			HashSet<Ticker> tickers = new HashSet<>();
			cmds.forEach(c -> tickers.add(this.tickerCommandConverter.convert(c)));
			iCompany.setTickers(tickers);
		}
		return iCompany;
	}

}
