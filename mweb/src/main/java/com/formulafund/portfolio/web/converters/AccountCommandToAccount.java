package com.formulafund.portfolio.web.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.User;
import com.formulafund.portfolio.web.commands.AccountCommand;

import lombok.Synchronized;

@Component
public class AccountCommandToAccount implements Converter<AccountCommand, Account> {
	private UserCommandToUser userCommandConverter;
	
	public AccountCommandToAccount(UserCommandToUser uConverter) {
		this.userCommandConverter = uConverter;
	}

	@Synchronized
	@Nullable
	@Override
	public Account convert(AccountCommand source) {
		if (source == null) {
			return null;
		}
		final Account account = new Account();
		account.setName(source.getName());
		User user = this.userCommandConverter.convert(source.getUserCommand());
		account.setUser(user);
		account.setId(source.getId());
		return account;
	}

}