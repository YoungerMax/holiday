package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherModel {
    @SerializedName("currentConditions")
    public WeatherCurrentModel currentConditions;

    @SerializedName("forecast")
    public Forecast forecast;

    @SerializedName("location")
    public WeatherLocationModel location;

    public static class Forecast {
        @SerializedName("days")
        public List<WeatherForecastModel> days;
    }
}
