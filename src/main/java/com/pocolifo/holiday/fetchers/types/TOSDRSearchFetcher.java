package com.pocolifo.holiday.fetchers.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.TOSDRSearchResultModel;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.util.List;

@RequiredArgsConstructor
public class TOSDRSearchFetcher implements Fetcher<List<TOSDRSearchResultModel>> {
    private final Holiday holiday;
    private List<TOSDRSearchResultModel> cached;

    private Request getRequest(String phrase) {
        HttpUrl url = HttpUrl.parse("https://api.tosdr.org/search/v4/")
                .newBuilder()
                .addQueryParameter("query", phrase)
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized List<TOSDRSearchResultModel> fetchData(String phrase) throws Exception {
        if (cached != null) return cached;

        try (Response response = holiday.httpClient.newCall(getRequest(phrase)).execute()) {
            JsonObject obj = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);
            JsonArray list = obj.get("parameters").getAsJsonObject().get("services").getAsJsonArray();

            List<TOSDRSearchResultModel> results = (List<TOSDRSearchResultModel>)
                    holiday.gson.fromJson(list,
                            TypeToken.getParameterized(List.class, TOSDRSearchResultModel.class));

            if (results.isEmpty()) throw new NotApplicableException();

            for (int i = 0; list.size() > i; i++) {
                TOSDRSearchResultModel result = results.get(i);
                result.serviceDataApiUrl = list.get(i).getAsJsonObject().get("links").getAsJsonObject().get("crisp")
                        .getAsJsonObject().get("api").getAsString();
            }

            return (cached = results);
        }
    }
}
