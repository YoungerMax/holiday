package com.pocolifo.holiday.fetchers.types;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pocolifo.holiday.Holiday;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.*;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DataUSAPlaceFetcher implements Fetcher<List<DataUSAPlaceDataYearModel>> {
    private final Holiday holiday;
    private List<DataUSAPlaceDataYearModel> cached;

    private Request getPopulationRequest(String geoId) {
        HttpUrl url = HttpUrl.parse("https://datausa.io/api/data")
                .newBuilder()
                .addQueryParameter("Geography", geoId)
                .addQueryParameter("measures", "Hispanic Population")
                .addQueryParameter("drilldowns", "Race,Ethnicity")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private Request getWagesRequest(String geoId) {
        HttpUrl url = HttpUrl.parse("https://datausa.io/api/data")
                .newBuilder()
                .addQueryParameter("Geography", geoId)
                .addQueryParameter("measures", "Household Income by Race")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private Request getPropertyValueRequest(String geoId) {
        HttpUrl url = HttpUrl.parse("https://acorn.datausa.io/api/data")
                .newBuilder()
                .addQueryParameter("Geography", geoId)
                .addQueryParameter("measures", "Property Value")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<DataUSAPlacePopulationModel> getPopulationData(DataUSASearchResultModel target) throws IOException {
        List<DataUSAPlacePopulationModel> populationData;

        try (Response response = holiday.httpClient.newCall(getPopulationRequest(target.id)).execute()) {
            JsonObject obj = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);

            populationData = (List<DataUSAPlacePopulationModel>) holiday.gson.fromJson(obj.get("data"),
                    TypeToken.getParameterized(List.class, DataUSAPlacePopulationModel.class));
        }

        return populationData;
    }

    @SuppressWarnings("unchecked")
    private List<DataUSAPlaceWageModel> getWageData(DataUSASearchResultModel target) throws IOException {
        List<DataUSAPlaceWageModel> data;

        try (Response response = holiday.httpClient.newCall(getWagesRequest(    target.id)).execute()) {
            JsonObject obj = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);

            data = (List<DataUSAPlaceWageModel>) holiday.gson.fromJson(obj.get("data"),
                    TypeToken.getParameterized(List.class, DataUSAPlaceWageModel.class));
        }

        return data;
    }

    @SuppressWarnings("unchecked")
    private List<DataUSAPlacePropertyValueModel> getPropertyValue(DataUSASearchResultModel target) throws IOException {
        List<DataUSAPlacePropertyValueModel> data;

        try (Response response = holiday.httpClient.newCall(getPropertyValueRequest(target.id)).execute()) {
            JsonObject obj = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);

            data = (List<DataUSAPlacePropertyValueModel>) holiday.gson.fromJson(obj.get("data"),
                    TypeToken.getParameterized(List.class, DataUSAPlacePropertyValueModel.class));
        }

        return data;
    }

    private DataUSASearchResultModel getTarget(String phrase) throws IOException {
        List<DataUSASearchResultModel> results = Fetchers.dataUSASearch.fetchData(phrase);

        if (results.isEmpty()) throw new NotApplicableException();

        DataUSASearchResultModel top = results.get(0);

        if (!top.type.equalsIgnoreCase("geo")) throw new NotApplicableException();

        return top;
    }

    private void formatData(DataUSASearchResultModel target, List<DataUSAPlaceWageModel> wageData, List<DataUSAPlacePopulationModel> populationData, List<DataUSAPlacePropertyValueModel> propertyValue) {
        List<DataUSAPlaceDataYearModel> years = new ArrayList<>();

        for (int i = 0; i < wageData.size(); i++) {
            DataUSAPlaceWageModel year = wageData.get(i);
            DataUSAPlaceDataYearModel current = new DataUSAPlaceDataYearModel();

            // Wages
            current.placeName = target.name;
            current.year = year.year;
            current.averageHouseholdIncome = year.averageIncome;
            current.medianPropertyValue = propertyValue.get(i).propertyValue;

            // Population
            populationData.stream().filter(entry -> entry.year == current.year).sorted((m1, m2) -> m2.population - m1.population).forEach(entry -> {
                current.population.put(new DataUSAPlacePopulationModel.RaceEthnicityPair(entry.race, entry.ethnicity), entry.population);
                current.totalPopulation += entry.population;
            });

            years.add(current);
        }

        if (years.isEmpty()) throw new NotApplicableException();

        cached = years;
    }

    @Override
    public synchronized List<DataUSAPlaceDataYearModel> fetchData(String phrase) throws IOException {
        if (cached != null) return cached;

        DataUSASearchResultModel target = getTarget(phrase);
        List<DataUSAPlaceWageModel> wageData = getWageData(target);
        List<DataUSAPlacePopulationModel> populationData = getPopulationData(target);
        List<DataUSAPlacePropertyValueModel> propertyValue = getPropertyValue(target);

        formatData(target, wageData, populationData, propertyValue);

        return cached;
    }
}
