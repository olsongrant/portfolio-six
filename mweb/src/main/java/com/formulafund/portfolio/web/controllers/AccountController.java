package com.formulafund.portfolio.web.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
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
    public String saveOrUpdate(@Valid @ModelAttribute("addaccount") AddAccountCommand command,
    						   BindingResult bindingResult){

    	log.info("AccountController::saveOrUpdate");
    	log.info("AddAccountCommand: " + command);
    	if (bindingResult.hasErrors()) {
    		return "user/accountadd";
    	}
    	Long idLong = Long.valueOf(command.getId());
    	ApplicationUser aUser = this.userService.findById(idLong);
    	Account account = Account.with(command.getAccountName(), aUser);
    	this.accountService.save(account);
        return "redirect:/user/" + aUser.getId() + "/show";
    }
    
	@RequestMapping("/user/{id}/addaccount")
	public String provideAddAccountForm(@PathVariable String id, Model model) {
		log.debug("provide Add Account Form for user id " + id);
		Long idLong = Long.valueOf(id);
		ApplicationUser user = this.userService.findById(idLong);
		AddAccountCommand command = new AddAccountCommand();
		command.setId(id);
		command.setUserFullName(user.getFullName());
		command.setUserHandle(user.getHandle());
		model.addAttribute("addaccount", command);
		return "user/accountadd";
	}
    
}
