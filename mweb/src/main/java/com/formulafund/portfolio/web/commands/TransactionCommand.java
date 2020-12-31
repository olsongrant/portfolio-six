package com.formulafund.portfolio.web.commands;

import java.time.LocalDateTime;

import com.formulafund.portfolio.data.model.TransactionType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionCommand {
	
    private Long id;
	private TickerCommand tickerCommand;
	private Float shareQuantity;
	private LocalDateTime transactionDateTime;
	
	private AccountCommand accountCommand;
    
	private TransactionType transactionType;

}
