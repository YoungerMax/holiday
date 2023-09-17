package com.pocolifo.holiday.fetchers.types;

import com.google.gson.JsonArray;
import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.DictionaryWordModel;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@RequiredArgsConstructor
public class DictionaryFetcher implements Fetcher<DictionaryWordModel> {
    private final Holiday holiday;
    private DictionaryWordModel cached = null;

    private Request getSearchRequest(String phrase) {
        HttpUrl url = HttpUrl.parse("https://api.dictionaryapi.dev/api/v2/entries/en/")
                .newBuilder()
                .addPathSegment(phrase)
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @Override
    public synchronized DictionaryWordModel fetchData(String phrase) throws IOException {
        if (cached != null) return cached;

        try (Response response = holiday.httpClient.newCall(getSearchRequest(phrase)).execute()) {
            if (response.code() != 200) throw new NotApplicableException();

            cached = holiday.gson.fromJson(
                    holiday.gson.fromJson(response.body().charStream(), JsonArray.class).get(0),
                    DictionaryWordModel.class);
        }

        if (cached.word == null) throw new NotApplicableException();

        return cached;
    }
}
