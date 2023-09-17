package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TOSDRSearchResultModel {
    @SerializedName("id")
    public int id;

    @SerializedName("is_comprehensively_reviewed")
    public boolean isComprehensivelyReviewed;

    @SerializedName("urls")
    public List<String> urls;

    @SerializedName("name")
    public String name;

    @SerializedName("rating")
    public TOSDRSearchRatingModel rating;

    @SerializedName("wikipedia")
    public String wikipediaUrl;

    public String serviceDataApiUrl;
}
