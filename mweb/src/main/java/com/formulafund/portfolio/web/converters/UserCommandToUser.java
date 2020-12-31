package com.formulafund.portfolio.web.converters;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.User;
import com.formulafund.portfolio.web.commands.AccountCommand;
import com.formulafund.portfolio.web.commands.UserCommand;

import lombok.Synchronized;

@Component
public class UserCommandToUser implements Converter<UserCommand, User> {
	
	private AccountCommandToAccount accountCommandConverter;
	
	public UserCommandToUser(@Lazy AccountCommandToAccount acConverter) {
		this.accountCommandConverter = acConverter;
	}

	@Synchronized
	@Nullable
	@Override
	public User convert(UserCommand source) {
		if (source == null) return null;
		User user = new User();
		user.setFullName(source.getFullName());
		user.setHandle(source.getHandle());
		Set<AccountCommand> cmds = source.getAccountCommands();
		if ((cmds != null) && (!cmds.isEmpty())) {
			HashSet<Account> accounts = new HashSet<>();
			cmds.forEach(e -> accounts.add(this.accountCommandConverter.convert(e)));
			user.setAccounts(accounts);
		}
		return user;
	}

}
