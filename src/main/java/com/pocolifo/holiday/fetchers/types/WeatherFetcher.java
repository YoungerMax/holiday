package com.pocolifo.holiday.fetchers.types;

import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.GlobeCoordinatesModel;
import com.pocolifo.holiday.models.WeatherModel;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class WeatherFetcher implements Fetcher<WeatherModel> {
    @RequiredArgsConstructor
    public enum TemperatureUnit {
        CELSIUS("celsius", "C"),
        FAHRENHEIT("fahrenheit", "F");

        public final String name;
        public final String abbreviated;
    }

    private final Holiday holiday;
    public TemperatureUnit unit;
    private WeatherModel cached;

    private Request getRequest(double latitude, double longitude, TemperatureUnit unit) {
        HttpUrl url = HttpUrl.parse("https://data.api.cnn.io/weather/getForecast")
                .newBuilder()
                .addPathSegment(String.format("%.2f,%.2f", latitude, longitude))
                .addPathSegment(unit.name)
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @Override
    public synchronized WeatherModel fetchData(String phrase) throws Exception {
        if (cached != null) return cached;

        GlobeCoordinatesModel coords = Fetchers.placeCoordinates.fetchData(phrase);

        try (Response response = holiday.httpClient.newCall(getRequest(coords.latitude, coords.longitude, unit)).execute()) {
            if (response.code() != 200) throw new NotApplicableException();

            cached = holiday.gson.fromJson(response.body().charStream(), WeatherModel.class);
        }

        if (cached == null) throw new NotApplicableException();

        return cached;
    }
}
