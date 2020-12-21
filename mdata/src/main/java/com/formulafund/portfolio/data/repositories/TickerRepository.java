package com.formulafund.portfolio.data.repositories;

import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.Ticker;

public interface TickerRepository extends CrudRepository<Ticker, Long> {

}
