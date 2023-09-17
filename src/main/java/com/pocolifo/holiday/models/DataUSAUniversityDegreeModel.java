package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;
import lombok.RequiredArgsConstructor;

public class DataUSAUniversityDegreeModel {
    @RequiredArgsConstructor
    public enum DegreeType {
        @SerializedName("Bachelors Degree")
        BACHELORS("Bachelors Degree");

        public final String name;
    }

    @SerializedName("CIP2")
    public String subject;

    @SerializedName("CIP4")
    public String fieldOfStudy;

    @SerializedName("CIP6")
    public String degreeName;

    @SerializedName("Degree")
    public DegreeType degreeType;

    @SerializedName("Completions")
    public int completions;

    @SerializedName("ID Year")
    public int year;
}
