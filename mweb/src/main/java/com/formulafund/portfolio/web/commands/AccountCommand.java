package com.formulafund.portfolio.web.commands;

import java.util.HashSet;
import java.util.Set;

import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountCommand {
    private Long id;
	private String name; 
	private Float originalCash;
	private Float currentCash;
	
	private UserCommand userCommand;
    
	@Override
	public String toString() {
		return "AccountCommand [id=" + id + ", name=" + name + ", originalCash=" + originalCash + ", currentCash="
				+ currentCash + ", userCommand=" + userCommand + "]";
	}

	private Set<TransactionCommand> transactionCommands = new HashSet<>();

}
