package com.formulafund.portfolio.web.commands;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCommand {
    private Long id;
	@Override
	public String toString() {
		return "UserCommand [id=" + id + ", fullName=" + firstName + " " + lastName + ", handle=" + handle + "]";
	}

	private String firstName;
	private String lastName;
	private String handle;
	
	private Set<AccountCommand> accountCommands = new HashSet<>();

}
