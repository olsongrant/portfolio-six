package com.formulafund.portfolio.data.services;

import java.util.Set;

import com.formulafund.portfolio.data.model.*;

public interface TransactionService extends CrudService<Transaction> {
	
	public Set<Transaction> purchasesForPortfolio(Portfolio aPortfolio); 

}
