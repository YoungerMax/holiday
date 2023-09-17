package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class RelatedNewsResultModel {
    @SerializedName("_score")
    public float score;

    @SerializedName("_source")
    public RelatedNewsArticleModel article;
}
