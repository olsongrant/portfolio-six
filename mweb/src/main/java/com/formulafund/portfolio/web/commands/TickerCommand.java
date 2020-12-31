package com.formulafund.portfolio.web.commands;

import com.formulafund.portfolio.data.model.Exchange;
import com.formulafund.portfolio.data.model.IssuingCompany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TickerCommand {
    private Long id;
	private IssuingCompanyCommand issuingCompanyCommand;
	private String symbol;
	private Exchange exchange;
}
