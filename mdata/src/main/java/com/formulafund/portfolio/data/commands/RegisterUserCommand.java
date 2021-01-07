package com.formulafund.portfolio.data.commands;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterUserCommand {
    @NotNull
    @NotEmpty
    private String firstName;
    
    @NotNull
    @NotEmpty
    private String lastName;
    
    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;
    
    @NotNull
    @NotEmpty
    private String email;

}
