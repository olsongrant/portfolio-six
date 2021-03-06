package com.formulafund.portfolio.web.commands;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddAccountCommand {
	private String id;
	@Override
	public String toString() {
		return "AddAccountCommand [id=" + id + ", accountName=" + accountName + ", userFullName=" + userFullName
				+ ", userHandle=" + userHandle + "]";
	}
	
	@NotEmpty
	private String accountName;
	private String userFullName;
	private String userHandle;
}
