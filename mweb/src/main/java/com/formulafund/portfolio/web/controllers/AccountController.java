package com.formulafund.portfolio.web.controllers;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formulafund.portfolio.data.commands.DeleteAccountCommand;
import com.formulafund.portfolio.data.commands.DeleteUserCommand;
import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.services.AccountService;
import com.formulafund.portfolio.data.services.TransactionService;
import com.formulafund.portfolio.data.services.UserService;
import com.formulafund.portfolio.web.commands.AddAccountCommand;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AccountController {
	
	private UserService userService;
	private AccountService accountService;
	private TransactionService transactionService;

	public AccountController(AccountService aService, 
							 UserService uService,
							 TransactionService aTransactionService) {
		this.accountService = aService;
		this.userService = uService;
		this.transactionService = aTransactionService;
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
    
	@GetMapping("/user/{id}/addaccount")
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
	
	@GetMapping("/account/{accountid}/delete")
	public String requestAccountDeleteForm(@PathVariable String accountid, Model model) {
		log.info("AccountController::provideAccountDeleteForm");
		log.info("account id: " + accountid);
		Account account = this.accountService.findById(Long.valueOf(accountid));
		if (account == null) {
			model.addAttribute("resultmessage", "Did not find the account for deletion.");
			return "simple";
		}
		ApplicationUser user = account.getUser();
		
		DeleteAccountCommand command = new DeleteAccountCommand();
		command.setAccountId(accountid);
		command.setAccountName(account.getName());
		command.setUserId(user.getId().toString());
		model.addAttribute("command", command);
		return "account/delete";
	}
    
	@PostMapping("/account/delete")
	public String deleteAccount(@Valid @ModelAttribute("command") DeleteAccountCommand command, 
			   BindingResult bindingResult, HttpServletRequest request, Model model) {
		log.info("AccountController::deleteAccount");
		log.info("accountid: " + command.getAccountId());
		Long idLong = Long.valueOf(command.getAccountId());
		Account account = this.accountService.findById(idLong);
		ApplicationUser user = account.getUser();
		String destination = "redirect:/user/" + user.getId() + "/show";
		Set<Transaction> transactions = account.getTransactions();  			
		log.info("Account " + account.getName() + " has " + transactions.size() + " to be deleted.");
		for (Transaction transaction: transactions) {
			log.info("Transaction to be deleted: " + transaction.toString());
			this.transactionService.delete(transaction);
		}
		this.accountService.delete(account);
		return destination;
	}
}
