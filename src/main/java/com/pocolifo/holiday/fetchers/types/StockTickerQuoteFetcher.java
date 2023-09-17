package com.pocolifo.holiday.fetchers.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.RelatedNewsResultModel;
import com.pocolifo.holiday.models.StockMarketQuoteModel;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class StockTickerQuoteFetcher implements Fetcher<StockMarketQuoteModel> {
    private final Holiday holiday;
    private StockMarketQuoteModel cache;

    private Request getRequest(String tickerSymbol) {
        HttpUrl url = HttpUrl.parse("https://query1.finance.yahoo.com/v7/finance/quote")
                .newBuilder()
                .addQueryParameter("symbols", tickerSymbol)
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @Override
    public synchronized StockMarketQuoteModel fetchData(String tickerSymbol) throws Exception {
        if (cache != null) return cache;

        try (Response response = holiday.httpClient.newCall(getRequest(tickerSymbol)).execute()) {
            JsonObject object = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);
            JsonArray results = object.get("quoteResponse").getAsJsonObject().get("result").getAsJsonArray();

            if (results.isEmpty()) throw new NotApplicableException();

            cache = holiday.gson.fromJson(results.get(0), StockMarketQuoteModel.class);

            if (cache == null) throw new NotApplicableException();
        }

        return cache;
    }
}
