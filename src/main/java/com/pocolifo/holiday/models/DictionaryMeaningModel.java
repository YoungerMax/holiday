package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DictionaryMeaningModel {
    @SerializedName("partOfSpeech")
    public String partOfSpeech;

    @SerializedName("definitions")
    public List<DictionaryDefinitionModel> definition;

    @SerializedName("synonyms")
    public String[] synonyms;

    @SerializedName("antonyms")
    public String[] antonyms;
}
