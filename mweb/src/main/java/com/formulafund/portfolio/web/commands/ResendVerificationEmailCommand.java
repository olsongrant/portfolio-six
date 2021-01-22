package com.formulafund.portfolio.web.commands;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResendVerificationEmailCommand {

	
	@Override
	public String toString() {
		return "ResetPasswordCommand [emailAddress=" + email + "]";
	}

	@NotEmpty
	private String email;

}
