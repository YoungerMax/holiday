package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataUSASearchResultModel {
    @SerializedName("alts")
    public List<String> alts;

    @SerializedName("profile")
    public String type;

    @SerializedName("keywords")
    public List<String> keywords;

    @SerializedName("name")
    public String name;

    @SerializedName("id")
    public String id;

    // `image` is excluded because it does not lead to the actual image; just a website which has
    // an image on it
}
