package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Exchange;
import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.repositories.TickerRepository;
import com.formulafund.portfolio.data.services.TickerService;

@Service
@Profile({"mysqldev", "h2dev", "mysqlprod"})
public class SDJPATickerService implements TickerService {
	
	private TickerRepository tickerRepository;
	
	public SDJPATickerService(TickerRepository tRepository) {
		this.tickerRepository = tRepository;
	}

	@Override
	public Set<Ticker> findAll() {
		return StreamSupport.stream(this.tickerRepository.findAll().spliterator(), false)
				.collect(Collectors.toSet());
	}

	@Override
	public Ticker findById(Long id) {
		return this.tickerRepository.findById(id).orElse(null);
	}

	@Override
	public Ticker save(Ticker object) {
		return this.tickerRepository.save(object);
	}

	@Override
	public void delete(Ticker object) {
		this.tickerRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		this.tickerRepository.deleteById(id);
	}


}
