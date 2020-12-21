package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.repositories.AccountRepository;
import com.formulafund.portfolio.data.services.AccountService;

@Service
@Profile("springdatajpa")
public class SDJPAAccountService implements AccountService {
	private AccountRepository accountRepository;
	
	public SDJPAAccountService(AccountRepository aRepository) {
		this.accountRepository = aRepository;
	}

	@Override
	public Set<Account> findAll() {
		Set<Account> accounts = new HashSet<>();
		this.accountRepository.findAll().forEach(accounts::add);
		return accounts;
	}

	@Override
	public Account findById(Long id) {
		return this.accountRepository.findById(id).orElse(null);
	}

	@Override
	public Account save(Account object) {
		return this.accountRepository.save(object);	
	}

	@Override
	public void delete(Account object) {
		this.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		this.deleteById(id);
	}

}
