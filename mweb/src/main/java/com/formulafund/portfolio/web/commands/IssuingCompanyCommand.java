package com.formulafund.portfolio.web.commands;

import java.util.HashSet;
import java.util.Set;

import com.formulafund.portfolio.data.model.Ticker;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IssuingCompanyCommand {
    private Long id;
	private String fullName;
	private Set<TickerCommand> tickerCommands = new HashSet<>();

}
