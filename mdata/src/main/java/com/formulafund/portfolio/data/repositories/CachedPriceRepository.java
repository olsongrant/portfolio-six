package com.formulafund.portfolio.data.repositories;

import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.CachedPrice;

public interface CachedPriceRepository extends CrudRepository<CachedPrice, Long> {

}
