package com.formulafund.portfolio.data.repositories;

import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.Portfolio;

public interface PortfolioRepository extends CrudRepository<Portfolio, Long> {

}
