package com.formulafund.portfolio.data.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteUserCommand {
	private String userId;
//	private String password;
	private boolean acknowledged;
	private String emailAddress;
	
	@Override
	public String toString() {
		return "DeleteUserCommand [userId=" + userId + ", acknowledged=" + acknowledged + ", emailAddress="
				+ emailAddress + "]";
	}
}
