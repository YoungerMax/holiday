package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TOSDRServiceModel {
    @SerializedName("id")
    public int id;

    @SerializedName("is_comprehensively_reviewed")
    public boolean isComprehensivelyReviewed;

    @SerializedName("name")
    public String name;

    @SerializedName("rating")
    public int rating;

    @SerializedName("wikipedia")
    public String wikipediaUrl;

    @SerializedName("points")
    public List<TOSDRPointModel> points;
}
