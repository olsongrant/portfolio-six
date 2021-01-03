package com.formulafund.portfolio.data.services.freestuff;

import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.PriceService;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;

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
	
	private static final String BASE_URL_STRING = 
			"https://bigcharts.marketwatch.com/quotes/multi.asp?view=q&msymb=";

	@Override
	public Float sharePriceForTicker(Ticker aTicker) {
		String symbol = aTicker.getSymbol();
		log.debug("symbol in sharePriceForTicker: " + symbol);
		return this.sharePriceForSymbol(symbol);	
	}

	public Float sharePriceForSymbol(String symbol) {
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
	
	protected Document documentForSymbol(String symbol) {
		String url = BigChartsQuoteService.BASE_URL_STRING + symbol;
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
		BigChartsQuoteService priceService = new BigChartsQuoteService();
		Float price = priceService.sharePriceForSymbol("NFLX");
		log.debug("Share price for NFLX: " + price);
	}

	static void investigateTechnique() {
		BigChartsQuoteService priceService = new BigChartsQuoteService();
//		priceService.sharePriceForSymbol("GOOG");
		Document doc = priceService.documentForSymbol("IBM");
		try (FileWriter writer = new FileWriter("bigcharts-ibm.html")) {
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
