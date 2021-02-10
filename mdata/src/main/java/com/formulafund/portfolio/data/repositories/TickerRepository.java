package com.formulafund.portfolio.data.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.Ticker;

public interface TickerRepository extends CrudRepository<Ticker, Long> {
	List<Ticker> findBySymbol(String aSymbol);
}
