package com.formulafund.portfolio.web.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.web.commands.TransactionCommand;

import lombok.Synchronized;

@Component
public class TransactionCommandToTransaction implements Converter<TransactionCommand, Transaction> {
	
	private AccountCommandToAccount accountCommandConverter;
	private TickerCommandToTicker tickerCommandConverter;
	
	public TransactionCommandToTransaction(AccountCommandToAccount acConverter,
			TickerCommandToTicker tcConverter) {
		this.accountCommandConverter = acConverter;
		this.tickerCommandConverter = tcConverter;
	}

	@Synchronized
	@Nullable
	@Override
	public Transaction convert(TransactionCommand source) {
		if (source == null) return null;
		Transaction txn = new Transaction();
		txn.setId(source.getId());
		txn.setShareQuantity(source.getShareQuantity());
		txn.setTicker(this.tickerCommandConverter.convert(source.getTickerCommand()));
		txn.setTransactionDateTime(source.getTransactionDateTime());
		txn.setTransactionType(source.getTransactionType());
		txn.setAccount(this.accountCommandConverter.convert(source.getAccountCommand()));
		return txn;
	}

}
