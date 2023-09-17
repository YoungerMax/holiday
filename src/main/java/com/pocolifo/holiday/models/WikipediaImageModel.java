package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class WikipediaImageModel {
    @SerializedName("source")
    public String url;

    @SerializedName("width")
    public int width;

    @SerializedName("height")
    public int height;
}
