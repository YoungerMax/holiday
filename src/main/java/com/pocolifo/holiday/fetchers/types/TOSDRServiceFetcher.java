package com.pocolifo.holiday.fetchers.types;

import com.google.gson.JsonObject;
import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.MediaWikiSearchResultModel;
import com.pocolifo.holiday.models.TOSDRSearchResultModel;
import com.pocolifo.holiday.models.TOSDRServiceModel;
import lombok.RequiredArgsConstructor;
import okhttp3.Request;
import okhttp3.Response;

import java.util.List;

@RequiredArgsConstructor
public class TOSDRServiceFetcher implements Fetcher<TOSDRServiceModel> {
    private final Holiday holiday;
    private TOSDRServiceModel cache;

    @Override
    public synchronized TOSDRServiceModel fetchData(String phrase) throws Exception {
        if (cache != null) return cache;
        // TODO: link search results with wikipedia link

        MediaWikiSearchResultModel wpResult = Fetchers.wikipediaSearch.fetchData(phrase).get(0);

        TOSDRSearchResultModel result = Fetchers.tosdrSearch.fetchData(phrase)
                .stream()
                .filter(m -> wpResult.url.equals(m.wikipediaUrl))
                .findFirst()
                .orElseThrow(NotApplicableException::new);

        Request request = new Request.Builder().url(result.serviceDataApiUrl).get().build();

        try (Response response = holiday.httpClient.newCall(request).execute()) {
            JsonObject object = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);
            TOSDRServiceModel service = holiday.gson.fromJson(object.get("parameters"), TOSDRServiceModel.class);

            cache = service;
            return service;
        }
    }
}
