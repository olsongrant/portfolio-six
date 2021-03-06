package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.model.Transaction;
import com.formulafund.portfolio.data.repositories.TransactionRepository;
import com.formulafund.portfolio.data.services.TransactionService;

@Service
@Profile({"mysqldev", "h2dev", "mysqlprod", "mysqlaws"})
public class SDJPATransactionService implements TransactionService {
	


	@Override
	public Set<Transaction> allTransactionsForAccount(Account anAccount) {
		List<Transaction> transactionList = this.transactionRepository.findByAccount(anAccount);
		HashSet<Transaction> transactions = new HashSet<>();
		transactionList.forEach(transactions::add);
		return transactions;
	}



	private TransactionRepository transactionRepository;
	
	public SDJPATransactionService(TransactionRepository tRepository) {
		this.transactionRepository = tRepository;
	}

	@Override
	public Set<Transaction> findAll() {
		return StreamSupport.stream(this.transactionRepository.findAll().spliterator(), false)
				.collect(Collectors.toSet());
	}

	@Override
	public Transaction findById(Long id) {
		return this.transactionRepository.findById(id).orElse(null);
	}

	@Override
	public Transaction save(Transaction object) {
		return this.transactionRepository.save(object);
	}

	@Override
	public void delete(Transaction object) {
		this.transactionRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		this.transactionRepository.deleteById(id);

	}
}
