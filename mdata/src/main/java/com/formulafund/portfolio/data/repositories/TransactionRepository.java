package com.formulafund.portfolio.data.repositories;

import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
