package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class WeatherCurrentModel {
    @SerializedName("visibility")
    public float visibility;

    @SerializedName("humidity")
    public int humidity;

    @SerializedName("wind")
    public String wind;

    @SerializedName("barometricPressure")
    public float barometricPressure;

    @SerializedName("temperature")
    public int temperature;

    @SerializedName("feelsLikeTemperature")
    public int feelsLikeTemperature;

    @SerializedName("dewPoint")
    public int dewPoint;

    @SerializedName("description")
    public String description;

    @SerializedName("lastUpdated")
    public WeatherTimeModel lastUpdatedTime;

    @SerializedName("sunsetTime")
    public WeatherTimeModel sunsetTime;

    @SerializedName("sunriseTime")
    public WeatherTimeModel sunriseTime;
}
