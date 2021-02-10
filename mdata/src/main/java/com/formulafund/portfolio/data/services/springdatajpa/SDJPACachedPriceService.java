package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.CachedPrice;
import com.formulafund.portfolio.data.repositories.CachedPriceRepository;
import com.formulafund.portfolio.data.services.CachedPriceService;


@Service
@Profile({"mysqldev", "h2dev", "mysqlprod", "mysqlaws"})
public class SDJPACachedPriceService implements CachedPriceService {
	
	@Override
	public Optional<CachedPrice> findBySymbol(String aSymbol) {
		List<CachedPrice> priceList = this.cachedPriceRepository.findByTickerSymbol(aSymbol);
		CachedPrice aPrice = priceList.size() > 0 ? priceList.get(0) : null;
		return Optional.ofNullable(aPrice);
	}

	private CachedPriceRepository cachedPriceRepository;
	
	public SDJPACachedPriceService(CachedPriceRepository aCachedPriceRepository) {
		this.cachedPriceRepository = aCachedPriceRepository;
	}

	@Override
	public Set<CachedPrice> findAll() {
		Set<CachedPrice> cachedPrices = new HashSet<>();
		this.cachedPriceRepository.findAll().forEach(cachedPrices::add);
		return cachedPrices;
	}

	@Override
	public CachedPrice findById(Long id) {
		return this.cachedPriceRepository.findById(id).orElse(null);
	}

	@Override
	public CachedPrice save(CachedPrice object) {
		return this.cachedPriceRepository.save(object);
	}

	@Override
	public void delete(CachedPrice object) {
		this.cachedPriceRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		this.cachedPriceRepository.deleteById(id);
	}



}
