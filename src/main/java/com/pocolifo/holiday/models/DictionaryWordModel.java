package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DictionaryWordModel {
    @SerializedName("word")
    public String word;

    @SerializedName("phonetic")
    public String phonetic;

    @SerializedName("phonetics")
    public List<DictionaryPhoneticModel> phonetics;

    @SerializedName("license")
    public DictionaryLicenseModel license;

    @SerializedName("meanings")
    public List<DictionaryMeaningModel> meanings;
}
