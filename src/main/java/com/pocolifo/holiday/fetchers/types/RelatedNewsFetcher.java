package com.pocolifo.holiday.fetchers.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.RelatedNewsResultModel;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RelatedNewsFetcher implements Fetcher<List<RelatedNewsResultModel>> {
    private final Holiday holiday;
    private List<RelatedNewsResultModel> cache;

    private Request getArticles(String phrase) {
        HttpUrl url = HttpUrl.parse("http://127.0.0.1:8000/search")
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
    public synchronized List<RelatedNewsResultModel> fetchData(String phrase) throws IOException {
        if (cache != null) return cache;

        try (Response response = holiday.httpClient.newCall(getArticles(phrase)).execute()) {
            JsonObject object = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);
            JsonArray hits = object.get("hits").getAsJsonObject().get("hits").getAsJsonArray();

            List<RelatedNewsResultModel> results = ((List<RelatedNewsResultModel>) holiday.gson.fromJson(hits, TypeToken.getParameterized(List.class, RelatedNewsResultModel.class)))
                    .stream()
                    .sorted(Comparator.comparingDouble(model -> model.score))
                    .collect(Collectors.toList());

            if (results.isEmpty()) throw new NotApplicableException();

            cache = results;
        }

        return cache;
    }
}
