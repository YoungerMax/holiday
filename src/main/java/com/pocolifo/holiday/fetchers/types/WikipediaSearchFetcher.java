package com.pocolifo.holiday.fetchers.types;

import com.google.gson.JsonArray;
import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.MediaWikiSearchResultModel;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class WikipediaSearchFetcher implements Fetcher<List<MediaWikiSearchResultModel>> {
    private final Holiday holiday;
    private List<MediaWikiSearchResultModel> results = null;

    private Request getSearchRequest(String phrase) {
        HttpUrl url = HttpUrl.parse("https://en.wikipedia.org/w/api.php")
                .newBuilder()
                .addQueryParameter("action", "opensearch")
                .addQueryParameter("format", "json")
                .addQueryParameter("formatversion", "2")
                .addQueryParameter("namespace", "0")
                .addQueryParameter("limit", "10")
                .addQueryParameter("search", phrase)
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @Override
    public synchronized List<MediaWikiSearchResultModel> fetchData(String phrase) throws IOException {
        // Check cache
        if (results != null) return results;

        // Get data
        List<MediaWikiSearchResultModel> results = new ArrayList<>();

        try (Response response = holiday.httpClient.newCall(getSearchRequest(phrase)).execute()) {
            JsonArray array = holiday.gson.fromJson(response.body().charStream(), JsonArray.class);
            JsonArray nameArray = array.get(1).getAsJsonArray();
            JsonArray linkArray = array.get(3).getAsJsonArray();

            for (int i = 0; nameArray.size() > i; i++) {
                String name = nameArray.get(i).getAsString();
                String link = linkArray.get(i).getAsString();
                String id = link.split("/")[4];

                results.add(new MediaWikiSearchResultModel(
                        id,
                        link,
                        name
                ));
            }
        }

        if (results.isEmpty()) throw new NotApplicableException();

        // Success! Cache results
        this.results = results;

        return this.results;
    }
}
