package com.formulafund.portfolio.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.User;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.web.commands.AddAccountCommand;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AccountController {
	
	private UserService userService;
	private AccountService accountService;

	public AccountController(AccountService aService, UserService uService) {
		this.accountService = aService;
		this.userService = uService;
	}
	
    @PostMapping("account")
    public String saveOrUpdate(@ModelAttribute AddAccountCommand command){
        // RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
    	System.out.println("AccountController::saveOrUpdate");
    	System.out.println("AddAccountCommand: " + command);
    	Long idLong = Long.valueOf(command.getId());
    	User aUser = this.userService.findById(idLong);
    	Account account = Account.with(command.getAccountName(), aUser);
    	this.accountService.save(account);
        return "redirect:/user/" + aUser.getId() + "/show";
    }
}
