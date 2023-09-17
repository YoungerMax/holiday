package com.pocolifo.holiday.models;

import java.util.HashMap;
import java.util.Map;

public class DataUSAPlaceDataYearModel {
    public String placeName;
    public int year;

    public Map<DataUSAPlacePopulationModel.RaceEthnicityPair, Integer> population = new HashMap<>();
    public int totalPopulation;

    public int averageHouseholdIncome;
    public int medianPropertyValue;
}
