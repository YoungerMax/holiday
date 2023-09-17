package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class DataUSAUniversityEnrollmentModel {
    @RequiredArgsConstructor
    public enum Race {
        @SerializedName("asian")
        ASIAN("Asian"),

        @SerializedName("black")
        BLACK("Black"),

        @SerializedName("hawaiian")
        HAWAIIAN_OTHER_PACIFIC_ISLANDER("Native Hawaiian/Other Pacific Islander"),

        @SerializedName("hispanic")
        HISPANIC_LATINO("Hispanic/Latino"),

        @SerializedName("multiracial")
        MULTIRACIAL("Multiracial"),

        @SerializedName("native")
        NATIVE_AMERICAS("Native North/Central/South American"),

        @SerializedName("nonresident")
        STUDYING_ABROAD("Studying Abroad"),

        @SerializedName("unknown")
        UNKNOWN("Unknown"),

        @SerializedName("white")
        WHITE("White");

        public final String name;
    }

    @SerializedName("ID IPEDS Race")
    public Race race;

    @SerializedName("ID Year")
    public int year;

    @SerializedName("Enrollment")
    public int enrollment;
}
