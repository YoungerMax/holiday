package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class WeatherTimeModel {
    @SerializedName("hours")
    public int hours;

    @SerializedName("minutes")
    public int minutes;

    @SerializedName("seconds")
    public int seconds;

    @SerializedName("time")
    public String timeString;

    @SerializedName("timezoneOffset")
    public int timezoneOffsetMinutesGMT;
}
