package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.Utility;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.models.StockMarketQuoteModel;

import static com.pocolifo.holiday.Utility.formatNumber;

public class StockMarketCard implements Card {
    @Override
    public String getName() {
        return "Stock Market";
    }

    @Override
    public String getCardHtml(String phrase) throws Exception {
        String tickerSymbol = Fetchers.stockTickerSymbol.fetchData(phrase);
        StockMarketQuoteModel quote = Fetchers.stockTickerQuote.fetchData(tickerSymbol);

        float percentChange = Utility.roundToNearestHundredth(quote.percentChange);
        float moneyChange = Utility.roundToNearestHundredth(quote.moneyChange);
        String percentChangeColor;

        if (percentChange > 0) {
            percentChangeColor = "#35bd08";
        } else if (0 > percentChange) {
            percentChangeColor = "#cf0c0c";
        } else {
            percentChangeColor = "#808080";
        }

        StringBuilder b = new StringBuilder();

        b.append("<h2>").append(quote.displayName).append(" (").append(tickerSymbol).append(")</h2>")
                .append("<h1>").append(String.format("%,.2f", quote.price)).append(" <small>").append(quote.currency).append("</small></h1>")
                .append("<h3 style=\"color: ").append(percentChangeColor).append(";\">").append(String.format("%+,.2f", moneyChange)).append(" <small>").append(quote.currency).append("</small> (").append(String.format("%+,.2f", percentChange)).append("%)</h3>")
                .append("<p>").append(quote.shortName).append(" \u2022 ").append(quote.sourceName).append(" \u2022 ").append(quote.sourceInterval).append(" minutes</p>");

        return b.toString();
    }
}
