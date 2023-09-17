package com.pocolifo.holiday.fetchers.types;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.models.DataUSASearchResultModel;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class DataUSASearchFetcher implements Fetcher<List<DataUSASearchResultModel>> {
    private final Holiday holiday;
    private List<DataUSASearchResultModel> cache = null;

    private Request getRequest(String phrase) {
        HttpUrl url = HttpUrl.parse("https://datausa.io/api/searchLegacy")
                .newBuilder()
                .addQueryParameter("limit", "5")
                .addQueryParameter("q", phrase)
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized List<DataUSASearchResultModel> fetchData(String phrase) throws IOException {
        if (cache != null) return cache;

        try (Response response = holiday.httpClient.newCall(getRequest(phrase)).execute()) {
            JsonObject obj = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);

            cache = (List<DataUSASearchResultModel>) holiday.gson.fromJson(obj.get("results"),
                    TypeToken.getParameterized(List.class, DataUSASearchResultModel.class));
        }

        return cache;
    }
}
