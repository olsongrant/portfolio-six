package com.formulafund.portfolio.data.commands;

import javax.validation.constraints.NotNull;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PasswordResetCommand {
    @NotNull
    @NotEmpty
    private String token;
    
    @NotNull
    @NotEmpty
    private String password;
    
    @NotEmpty
    private String matchingPassword;
    
    @NotNull
    @NotEmpty
    private String email;


}
