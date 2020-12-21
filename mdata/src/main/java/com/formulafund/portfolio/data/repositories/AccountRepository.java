package com.formulafund.portfolio.data.repositories;

import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {

}
