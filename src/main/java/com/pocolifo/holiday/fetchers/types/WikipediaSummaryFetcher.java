package com.pocolifo.holiday.fetchers.types;

import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.MediaWikiSearchResultModel;
import com.pocolifo.holiday.models.WikipediaSummaryModel;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class WikipediaSummaryFetcher implements Fetcher<WikipediaSummaryModel> {
    private final Holiday holiday;
    private WikipediaSummaryModel summary = null;

    private Request getSummaryRequest(String wikipediaId) {
        HttpUrl url = HttpUrl.parse("https://en.wikipedia.org/api/rest_v1/page/summary/")
                .newBuilder()
                .addPathSegment(wikipediaId)
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @Override
    public synchronized WikipediaSummaryModel fetchData(String phrase) throws IOException {
        // Check cache
        if (summary != null) return summary;

        // Get data
        List<MediaWikiSearchResultModel> results = Fetchers.wikipediaSearch.fetchData(phrase);

        if (results.isEmpty() || results.get(0) == null) throw new NotApplicableException();

        try (Response response = holiday.httpClient.newCall(getSummaryRequest(results.get(0).id)).execute()) {
            // Success! Cache results
            summary = holiday.gson.fromJson(response.body().charStream(), WikipediaSummaryModel.class);
        }

        if (summary == null) throw new NotApplicableException();

        return summary;
    }
}
