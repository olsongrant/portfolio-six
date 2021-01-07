package com.formulafund.portfolio.web.converters;

import java.util.Set;
import java.util.HashSet;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.web.commands.AccountCommand;
import com.formulafund.portfolio.web.commands.*;

import lombok.Synchronized;

@Component
public class AccountToAccountCommand implements Converter<Account, AccountCommand> {
	
	private final TransactionToTransactionCommand transactionConverter;
	private final UserToUserCommand userConverter;
	
	public AccountToAccountCommand(TransactionToTransactionCommand tConverter,
			UserToUserCommand uConverter) {
		this.transactionConverter = tConverter;
		this.userConverter = uConverter;
	}

	@Synchronized
	@Nullable
	@Override
	public AccountCommand convert(Account source) {
		if (source == null) return null;
		AccountCommand cmd = new AccountCommand();
		cmd.setCurrentCash(source.getCurrentCash());
		cmd.setId(source.getId());
		cmd.setName(source.getName());
		cmd.setOriginalCash(source.getOriginalCash());
		Set<Transaction> transactions = source.getTransactions();
		if ( (transactions != null) && (!transactions.isEmpty())) {
			HashSet<TransactionCommand> commands = new HashSet<>();
			transactions.forEach(t -> commands.add(this.transactionConverter.convert(t)));
			cmd.setTransactionCommands(commands);
		}
		ApplicationUser user = source.getUser();
		UserCommand uc = this.userConverter.convert(user);
		cmd.setUserCommand(uc);
		return cmd;
	}

}
