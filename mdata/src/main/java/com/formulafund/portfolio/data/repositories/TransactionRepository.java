package com.formulafund.portfolio.data.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	List<Transaction> findByAccount(Account anAccount);
	
//	List<Transaction> findSalesByAccount(Account anAccount);
	
//	List<Transaction> findPurchasesByAccount(Account anAccount);
	
}
