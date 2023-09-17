package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class DictionaryPhoneticModel {
    @SerializedName("text")
    public String phonetic;

    @SerializedName("audio")
    public String audioUrl;

    @SerializedName("sourceUrl")
    public String sourceUrl;
}
