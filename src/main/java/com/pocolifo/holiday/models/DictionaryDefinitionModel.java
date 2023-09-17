package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class DictionaryDefinitionModel {
    @SerializedName("definition")
    public String definition;

    @SerializedName("synonyms")
    public String[] synonyms;

    @SerializedName("antonyms")
    public String[] antonyms;

    @SerializedName("example")
    public String exampleSentence;
}
