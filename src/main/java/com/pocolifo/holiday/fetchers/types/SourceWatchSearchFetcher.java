package com.pocolifo.holiday.fetchers.types;

import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.MediaWikiSearchResultModel;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@RequiredArgsConstructor
public class SourceWatchSearchFetcher implements Fetcher<MediaWikiSearchResultModel> {
    private final Holiday holiday;
    private MediaWikiSearchResultModel model;

    private Request getSearchRequest(String phrase) {
        HttpUrl url = HttpUrl.parse("https://www.sourcewatch.org/index.php")
                .newBuilder()
                .addQueryParameter("title", "Special:Search")
                .addQueryParameter("search", phrase)
                .addQueryParameter("go", "Go")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @Override
    public synchronized MediaWikiSearchResultModel fetchData(String phrase) throws IOException {
        // get cache
        if (model != null) return model;

        // get data
        String location;

        try (Response response = this.holiday.httpClientNoRedir.newCall(getSearchRequest(phrase)).execute()) {
            location = response.header("Location");
        }

        // search did not return anything, not applicable
        if (location == null) throw new NotApplicableException();

        String id = location.replace("https://www.sourcewatch.org/index.php?title=", "");

        MediaWikiSearchResultModel model = new MediaWikiSearchResultModel(
                id,
                location,
                id.replace("_", " ")
        );

        // return
        this.model = model;

        return model;
    }
}
