package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class RelatedNewsArticleModel {
    @SerializedName("title")
    public String title;

    @SerializedName("url")
    public String url;

    @SerializedName("description")
    public String description;

    @SerializedName("published")
    public String date;

    @SerializedName("thumbnail_url")
    public String thumbnailUrl;

    @SerializedName("source")
    public RelatedNewsSourceModel source;

    @SerializedName("keywords")
    public List<String> keywords;
}
