package com.formulafund.portfolio.data.commands;

import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SocialUserCommand {
    @NotNull
    @NotEmpty
    private String firstName;
    
    @NotNull
    @NotEmpty
    private String lastName;
    
    @NotNull
    @NotEmpty
    private String email;
    
    @NotNull
    @NotEmpty
    private String handle;
    
    private String socialPlatformId;

	@Override
	public String toString() {
		return "SocialUserCommand [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", handle="
				+ handle + ", socialPlatformId=" + socialPlatformId + "]";
	}

}
