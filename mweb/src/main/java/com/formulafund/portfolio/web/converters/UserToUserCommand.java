package com.formulafund.portfolio.web.converters;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.formulafund.portfolio.web.commands.*;
import com.formulafund.portfolio.data.model.*;
import java.util.*;

@Component
public class UserToUserCommand implements Converter<User, UserCommand> {
	
	private AccountToAccountCommand accountConverter;
	
	public UserToUserCommand(@Lazy AccountToAccountCommand aConverter) {
		this.accountConverter = aConverter;
	}

	@Override
	public UserCommand convert(User source) {
		if (source == null) return null;
		UserCommand cmd = new UserCommand();
		Set<Account> accounts = source.getAccounts();
		if ((accounts != null) && (!accounts.isEmpty())) {
			HashSet<AccountCommand> commands = new HashSet<>();
			accounts.forEach(a -> commands.add(this.accountConverter.convert(a)));
			cmd.setAccountCommands(commands);
		}
		cmd.setFullName(source.getFullName());
		cmd.setHandle(source.getHandle());
		cmd.setId(source.getId());
		return cmd;
	}

}
