package com.pocolifo.holiday.fetchers.types;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ImseaImageFetcher implements Fetcher<List<String>> {
    private final Holiday holiday;
    private List<String> cached;

    private Request getSearchRequest(String phrase) {
        HttpUrl url = HttpUrl.parse("https://imsea.herokuapp.com/api/1")
                .newBuilder()
                .addQueryParameter("q", phrase)
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized List<String> fetchData(String phrase) throws IOException {
        if (cached != null) return cached;

        try (Response response = holiday.httpClient.newCall(getSearchRequest(phrase)).execute()) {
            JsonObject object = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);
            cached = (List<String>) holiday.gson.fromJson(object.get("results"), TypeToken.getParameterized(List.class, String.class));
            cached = cached.stream().distinct().collect(Collectors.toList());
        }

        return cached;
    }
}
