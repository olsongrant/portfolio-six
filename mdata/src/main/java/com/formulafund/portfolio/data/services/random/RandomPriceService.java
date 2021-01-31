package com.formulafund.portfolio.data.services.random;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.PriceService;

@Service
public class RandomPriceService implements PriceService {
	private Random random = new Random();

	@Override
	public Float sharePriceForTicker(Ticker aTicker) {
		return this.sharePriceForSymbol("Makes no difference");
	}
	
	public Float sharePriceForSymbol(String aSymbol) {
		Float basePrice = 16.0f;
		Float ourRandom = random.nextFloat();
		return (basePrice * ourRandom) + basePrice;
	}

}
