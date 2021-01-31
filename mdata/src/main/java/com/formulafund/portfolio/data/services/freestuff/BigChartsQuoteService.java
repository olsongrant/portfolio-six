package com.formulafund.portfolio.data.services.freestuff;

import com.formulafund.portfolio.data.model.CachedPrice;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.CachedPriceService;
import com.formulafund.portfolio.data.services.PriceService;
import com.formulafund.portfolio.data.services.random.UnhelpfulCachedPriceService;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Primary
public class BigChartsQuoteService implements PriceService {
	
	private CachedPriceService cachedPriceService;
	
	public BigChartsQuoteService(CachedPriceService aCachedPriceService) {
		this.cachedPriceService = aCachedPriceService;
	}
	
	private static final String BASE_URL_STRING = 
			"https://bigcharts.marketwatch.com/quotes/multi.asp?view=q&msymb=";

	@Override
	public Float sharePriceForTicker(Ticker aTicker) {
		String symbol = aTicker.getSymbol();
		log.debug("symbol in sharePriceForTicker: " + symbol);
		return this.sharePriceForSymbol(symbol);	
	}

	public Float sharePriceForSymbol(String symbol) {
		Optional<CachedPrice> potentialCachedPrice = this.cachedPriceService.findBySymbol(symbol);
		if (potentialCachedPrice.isPresent()) {
			CachedPrice cached = potentialCachedPrice.get();
			if (LocalDateTime.now().minusMinutes(5L).isBefore(cached.getTimestamp())) {
				log.info("returning cached price for symbol " + symbol);
				return cached.getLatestPrice();
			}
		}
		Document doc = this.documentForSymbol(symbol);
		Elements elements = doc.getElementsByClass("zebra");
		if (elements.size() < 1) { 
			log.debug("no class=zebra elements");
			return null;
		}
		Element ourRow = elements.first();
		Elements lastCols = ourRow.getElementsByClass("last-col");
		if (lastCols.size() < 1) {
			log.debug("no class=last-col elements");
			return null;
		}
		Element lastCol = lastCols.first();
		log.debug("last-col contents: " + lastCol.text());
		String valueWithDressing = lastCol.text();
		String valueWithoutDressing = valueWithDressing.replaceAll(",", "");
		return Float.valueOf(valueWithoutDressing);
	}
	
	public Map<String, Float> pricesForSymbols(List<String> aSymbolList) {
		HashMap<String, Float> priceMap = new HashMap<>();
		List<String> symbols = List.copyOf(aSymbolList);
		String joinedSymbols = symbols.stream().collect(Collectors.joining("+"));
		log.info("joined symbol list: " + joinedSymbols);
		Document doc = this.documentForSymbol(joinedSymbols);
		Elements elements = doc.getElementsByClass("multiquote quick");
		if (elements.size() < 1) { 
			log.warn("no 'multiquote quick' elements");
			return null;
		}		
		Element ourTable = elements.first();
		Elements tbodies = ourTable.getElementsByTag("tbody");
		if (tbodies.size() < 1) {
			log.warn("no tbody elements!");
			return null;
		}
		Element tbody = tbodies.first();
		Elements tableRows = tbody.getElementsByTag("tr");
		Iterator<Element> trElements = tableRows.iterator();
		while (trElements.hasNext()) {
			Element trElement = trElements.next();
			Elements symbColElements = trElement.getElementsByClass("symb-col");
			if (symbColElements.size() < 1) {
				log.info("looked for symb-col class element in a tr element, but did not find.");
				continue;
			}
			Element tdSymbolElement = symbColElements.first();
			String symbol = tdSymbolElement.text();
			Elements lastColElements = trElement.getElementsByClass("last-col");
			if (lastColElements.size() < 1) {
				log.info("looked for last-col class element in a tr element, but did not find.");
				continue;
			}
			Element lastColElement = lastColElements.first();
			String lastPriceString = lastColElement.text();
			lastPriceString = lastPriceString.replaceAll(",", "");
			Float lastPrice = Float.valueOf(lastPriceString);
			priceMap.put(symbol, lastPrice);
		}
		return priceMap;
	}
	
	protected Document documentForSymbol(String symbolList) {
		String url = BigChartsQuoteService.BASE_URL_STRING + symbolList;
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			log.debug("Title: " + doc.title());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	public static void main(String[] args) {
//		investigateTechnique();
		BigChartsQuoteService priceService = new BigChartsQuoteService(new UnhelpfulCachedPriceService());
//		Float price = priceService.sharePriceForSymbol("NFLX");
//		log.debug("Share price for NFLX: " + price);
		List<String> symbolList = List.of("IBM", "GOOG", "QQQ");
		
		Map<String, Float> priceMap = priceService.pricesForSymbols(symbolList);
		priceMap.forEach((symbol, price) -> System.out.println(symbol + " --> " + price));
	}

	static void investigateTechnique() {
		BigChartsQuoteService priceService = new BigChartsQuoteService(new UnhelpfulCachedPriceService());
//		priceService.sharePriceForSymbol("GOOG");
		Document doc = priceService.documentForSymbol("IBM+GOOG+QQQ");
		try (FileWriter writer = new FileWriter("bigcharts-ibm-plus.html")) {
			writer.write(doc.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements elements = doc.getElementsByClass("zebra");
		if (elements.size() < 1) { 
			log.debug("no class=zebra elements");
			return;
		}
		Element ourRow = elements.first();
		Elements lastCols = ourRow.getElementsByClass("last-col");
		if (lastCols.size() < 1) {
			log.debug("no class=last-col elements");
			return;
		}
		Element lastCol = lastCols.first();
		log.debug("last-col contents: " + lastCol.text());
	}
}
