package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

public class DataUSAUniversityGeneralYearModel {
    @SerializedName("ID Year")
    public int year;

    @SerializedName("Name")
    public String name;

    @SerializedName("Admissions Total")
    public int totalAdmissions;

    @SerializedName("Applicants Total")
    public int totalApplicants;

    @SerializedName("SAT Critical Reading 25th Percentile")
    public short satReading25thPercentile;

    @SerializedName("SAT Critical Reading 75th Percentile")
    public short satReading75thPercentile;

    @SerializedName("SAT Math 25th Percentile")
    public short satMath25thPercentile;

    @SerializedName("SAT Math 75th Percentile")
    public short satMath75thPercentile;

    @SerializedName("State Tuition")
    public int tuitionCost;
}
