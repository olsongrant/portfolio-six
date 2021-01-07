package com.formulafund.portfolio.web.converters;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.web.commands.AccountCommand;
import com.formulafund.portfolio.web.commands.UserCommand;

import lombok.Synchronized;

@Component
public class UserCommandToUser implements Converter<UserCommand, ApplicationUser> {
	
	private AccountCommandToAccount accountCommandConverter;
	
	public UserCommandToUser(@Lazy AccountCommandToAccount acConverter) {
		this.accountCommandConverter = acConverter;
	}

	@Synchronized
	@Nullable
	@Override
	public ApplicationUser convert(UserCommand source) {
		if (source == null) return null;
		ApplicationUser user = new ApplicationUser();
		user.setFirstName(source.getFirstName());
		user.setLastName(source.getLastName());
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
