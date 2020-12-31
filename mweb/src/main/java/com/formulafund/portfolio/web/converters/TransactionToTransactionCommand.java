package com.formulafund.portfolio.web.converters;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.web.commands.TransactionCommand;

import lombok.Synchronized;

@Component
public class TransactionToTransactionCommand implements Converter<Transaction, TransactionCommand> {
	
	private AccountToAccountCommand accountConverter;
	private TickerToTickerCommand tickerConverter;
	
	public TransactionToTransactionCommand(@Lazy AccountToAccountCommand aConverter,
			@Lazy TickerToTickerCommand tConverter) {
		this.accountConverter = aConverter;
		this.tickerConverter = tConverter;
	}

	@Synchronized
	@Nullable
	@Override
	public TransactionCommand convert(Transaction source) {
		if (source == null) return null;
		TransactionCommand txCommand = new TransactionCommand();
		txCommand.setId(source.getId());
		txCommand.setAccountCommand(this.accountConverter.convert(source.getAccount()));
		txCommand.setShareQuantity(source.getShareQuantity());
		txCommand.setTickerCommand(this.tickerConverter.convert(source.getTicker()));
		txCommand.setTransactionDateTime(source.getTransactionDateTime());
		txCommand.setTransactionType(source.getTransactionType());
		return txCommand;
	}

}
