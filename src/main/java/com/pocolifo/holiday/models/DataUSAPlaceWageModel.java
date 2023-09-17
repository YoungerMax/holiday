package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class DataUSAPlaceWageModel {
    @SerializedName("ID Year")
    public int year;

    @SerializedName("Household Income by Race")
    public int averageIncome;
}
