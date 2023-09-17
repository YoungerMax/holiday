package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;
import lombok.RequiredArgsConstructor;

public class DataUSAPlacePopulationModel {
    @RequiredArgsConstructor
    public enum Race {
        @SerializedName("White Alone")
        WHITE("White"),

        @SerializedName("Black or African American Alone")
        BLACK("Black"),

        @SerializedName("American Indian & Alaska Native Alone")
        NATIVE_AMERICAS("Native North/Central/South American"),

        @SerializedName("Asian Alone")
        ASIAN("Asian"),

        @SerializedName("Native Hawaiian & Other Pacific Islander Alone")
        HAWAIIAN_OTHER_PACIFIC_ISLANDER("Native Hawaiian/Other Pacific Islander"),

        @SerializedName("Some Other Race Alone")
        OTHER("Other"),

        @SerializedName("Two or More Races")
        MULTIRACIAL("Multiracial");

        public final String name;
    }

    @RequiredArgsConstructor
    public enum Ethnicity {
        @SerializedName("Not Hispanic or Latino")
        NON_HISPANIC_LATINO("Not Hispanic/Latino"),

        @SerializedName("Hispanic or Latino")
        HISPANIC_LATINO("Hispanic/Latino");

        public final String name;
    }

    @RequiredArgsConstructor
    public static class RaceEthnicityPair {
        public final Race race;
        public final Ethnicity ethnicity;

        @Override
        public boolean equals(Object o) {
            if (o instanceof RaceEthnicityPair) {
                RaceEthnicityPair pair = (RaceEthnicityPair) o;

                return pair.ethnicity.equals(this.ethnicity) && pair.race.equals(this.race);
            }

            return false;
        }
    }

    @SerializedName("Race")
    public Race race;

    @SerializedName("Ethnicity")
    public Ethnicity ethnicity;

    @SerializedName("ID Year")
    public int year;

    @SerializedName("Hispanic Population")
    public int population;
}