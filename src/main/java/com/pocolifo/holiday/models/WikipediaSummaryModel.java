package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class WikipediaSummaryModel {
    @SerializedName("title")
    public String title;

    @SerializedName("wikibase_item")
    public String wikibaseItem;

    @SerializedName("thumbnail")
    public WikipediaImageModel thumbnail;

    @SerializedName("orginalimage")
    public WikipediaImageModel originalImage;

    @SerializedName("description")
    public String description;

    @SerializedName("extract")
    public String extract;

    @SerializedName("extract_html")
    public String extractHtml;
}
