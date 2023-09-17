package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class TOSDRPointModel {
    public enum TOSDRPointStatus {
        @SerializedName("declined")
        DECLINED,

        @SerializedName("pending")
        PENDING,

        @SerializedName("approved")
        APPROVED;
    }

    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("source")
    public String source;

    @SerializedName("status")
    public TOSDRPointStatus status;

    @SerializedName("quoteText")
    public String quote;
}
