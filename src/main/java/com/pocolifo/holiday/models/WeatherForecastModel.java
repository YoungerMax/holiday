package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class WeatherForecastModel {
    @SerializedName("high")
    public int high;

    @SerializedName("low")
    public int low;

    @SerializedName("humidity")
    public int humidity;

    @SerializedName("wind")
    public String wind;

    @SerializedName("description")
    public String description;

    @SerializedName("descriptionNight")
    public String descriptionNight;

    @SerializedName("dayName")
    public String dayOfTheWeek;

    @SerializedName("sunsetTime")
    public WeatherTimeModel sunsetTime;

    @SerializedName("sunriseTime")
    public WeatherTimeModel sunriseTime;
}
