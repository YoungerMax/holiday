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
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class DataUSAUniversityFetcher implements Fetcher<List<DataUSAUniversityDataYearModel>> {
    private static final String[] MEASURES = {
            "Admissions Total",
            "Applicants Total",
            "State Tuition",
            "SAT Critical Reading 25th Percentile",
            "SAT Critical Reading 75th Percentile",
            "SAT Math 25th Percentile",
            "SAT Math 75th Percentile"
    };

    private final Holiday holiday;
    private List<DataUSAUniversityDataYearModel> cached = null;

    ////////////////////////////////
    // METHODS THAT GET HTTP URLS //
    ////////////////////////////////

    private Request getDataByYearRequest(String universityId) {
        HttpUrl url = HttpUrl.parse("https://datausa.io/api/data")
                .newBuilder()
                .addQueryParameter("University", universityId)
                .addQueryParameter("measures", String.join(",", MEASURES))
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private Request getEnrollmentRequest(String universityId) {
        HttpUrl url = HttpUrl.parse("https://datausa.io/api/data")
                .newBuilder()
                .addQueryParameter("University", universityId)
                .addQueryParameter("measures", "Enrollment")
                .addQueryParameter("drilldowns", "IPEDS Race")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private Request getDegreeRequest(String universityId) {
        HttpUrl url = HttpUrl.parse("https://datausa.io/api/data")
                .newBuilder()
                .addQueryParameter("University", universityId)
                .addQueryParameter("measures", "Completions")
                .addQueryParameter("drilldowns", "CIP6")
                .addQueryParameter("Degree", "5")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    // TODO: occupation and wage
    private Request getOccupationRequest(String universityId) {
        HttpUrl url = HttpUrl.parse("https://datausa.io/api/data")
                .newBuilder()
                .addQueryParameter("University", universityId)
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();

//        CIP2=11
//        Detailed Occupation=151252,1191XX,1721YY,172051,251000
//        measures=Total Population,Average Wage,Average Wage Appx MOE,Total Population MOE Appx,Record Count
//        Record Count>=5
//        Workforce Status=true
    }

    ///////////////////////////////
    // GET TOP UNIVERSITY RESULT //
    ///////////////////////////////

    private DataUSASearchResultModel getTarget(String phrase) throws IOException {
        List<DataUSASearchResultModel> results = Fetchers.dataUSASearch.fetchData(phrase);

        if (results.isEmpty()) throw new NotApplicableException();

        DataUSASearchResultModel top = results.get(0);

        if (!top.type.equalsIgnoreCase("university")) throw new NotApplicableException();

        return top;
    }


    ///////////////////////////
    // METHODS THAT GET DATA //
    ///////////////////////////

    @SuppressWarnings("unchecked")
    private List<DataUSAUniversityGeneralYearModel> getGeneralData(DataUSASearchResultModel target) throws IOException {
        List<DataUSAUniversityGeneralYearModel> generalData;

        try (Response response = holiday.httpClient.newCall(getDataByYearRequest(target.id)).execute()) {
            JsonObject obj = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);

            generalData = (List<DataUSAUniversityGeneralYearModel>) holiday.gson.fromJson(obj.get("data"),
                    TypeToken.getParameterized(List.class, DataUSAUniversityGeneralYearModel.class));
        }

        return generalData;
    }

    @SuppressWarnings("unchecked")
    private List<DataUSAUniversityEnrollmentModel> getEnrollmentData(DataUSASearchResultModel target) throws IOException {
        List<DataUSAUniversityEnrollmentModel> enrollmentData;

        try (Response response = holiday.httpClient.newCall(getEnrollmentRequest(target.id)).execute()) {
            JsonObject obj = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);

            enrollmentData = (List<DataUSAUniversityEnrollmentModel>) holiday.gson.fromJson(obj.get("data"),
                    TypeToken.getParameterized(List.class, DataUSAUniversityEnrollmentModel.class));
        }

        return enrollmentData;
    }

    @SuppressWarnings("unchecked")
    private List<DataUSAUniversityDegreeModel> getDegreeData(DataUSASearchResultModel target) throws IOException {
        List<DataUSAUniversityDegreeModel> degreeData;

        try (Response response = holiday.httpClient.newCall(getDegreeRequest(target.id)).execute()) {
            JsonObject obj = holiday.gson.fromJson(response.body().charStream(), JsonObject.class);

            degreeData = (List<DataUSAUniversityDegreeModel>) holiday.gson.fromJson(obj.get("data"),
                    TypeToken.getParameterized(List.class, DataUSAUniversityDegreeModel.class));
        }

        return degreeData;
    }


    //////////////////////
    // FORMAT THAT DATA //
    //////////////////////

    private void formatData(DataUSASearchResultModel target, List<DataUSAUniversityGeneralYearModel> generalData, List<DataUSAUniversityEnrollmentModel> enrollmentData, List<DataUSAUniversityDegreeModel> degreeData) {
        List<DataUSAUniversityDataYearModel> years = new ArrayList<>();

        for (DataUSAUniversityGeneralYearModel year : generalData) {
            DataUSAUniversityDataYearModel current = new DataUSAUniversityDataYearModel();

            // General
            current.universityName = target.name;
            current.year = year.year;
            current.generalData = year;

            // Enrollment
            enrollmentData.stream().filter(entry -> entry.year == current.year).forEach(entry -> {
                current.enrollmentByRace.put(entry.race, entry.enrollment);
                current.totalEnrollment += entry.enrollment;
            });

            // Degree
            degreeData.stream().filter(entry -> entry.year == current.year).forEach(entry -> {
                current.degreeData.add(entry);
                current.totalDegreeCompletions += entry.completions;
            });

            current.degreeData.sort((m1, m2) -> m2.completions - m1.completions);

            years.add(current);
        }

        if (years.isEmpty()) throw new NotApplicableException();

        cached = years;
    }

    ///////////////////
    // DO EVERYTHING //
    ///////////////////

    @Override
    public synchronized List<DataUSAUniversityDataYearModel> fetchData(String phrase) throws IOException {
        if (cached != null) return cached;

        DataUSASearchResultModel target = getTarget(phrase);
        List<DataUSAUniversityGeneralYearModel> generalData = getGeneralData(target);
        List<DataUSAUniversityEnrollmentModel> enrollmentData = getEnrollmentData(target);
        List<DataUSAUniversityDegreeModel> degreeData = getDegreeData(target);

        formatData(target, generalData, enrollmentData, degreeData);

        return cached;
    }
}
