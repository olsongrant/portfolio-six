package com.formulafund.portfolio.data.services.random;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import com.formulafund.portfolio.data.model.CachedPrice;
import com.formulafund.portfolio.data.services.CachedPriceService;

public class UnhelpfulCachedPriceService implements CachedPriceService {

	@Override
	public Optional<CachedPrice> findBySymbol(String aSymbol) {
		CachedPrice cachedPrice = new CachedPrice();
		cachedPrice.setTimestamp(LocalDateTime.MIN);
		cachedPrice.setTickerSymbol(aSymbol);
		cachedPrice.setLatestPrice(10.0f);
		return Optional.of(cachedPrice);
	}

	@Override
	public Set<CachedPrice> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CachedPrice findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CachedPrice save(CachedPrice object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(CachedPrice object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub

	}

}
