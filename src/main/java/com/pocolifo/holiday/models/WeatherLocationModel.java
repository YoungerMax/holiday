package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class WeatherLocationModel {
    @SerializedName("displayName")
    public String name;

    @SerializedName("adminDistrict")
    public String adminDistrict;

    @SerializedName("country")
    public String country;

    @SerializedName("postalCode")
    public String postalCode;

    @SerializedName("formattedName")
    public String formattedName;
}
