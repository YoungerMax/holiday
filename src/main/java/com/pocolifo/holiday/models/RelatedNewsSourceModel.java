package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class RelatedNewsSourceModel {
    @SerializedName("name")
    public String name;

    @SerializedName("url")
    public String url;

    @SerializedName("logo_url")
    public String logoUrl;
}
