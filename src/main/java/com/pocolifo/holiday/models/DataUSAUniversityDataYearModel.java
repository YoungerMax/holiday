package com.pocolifo.holiday.models;

import java.util.*;

public class DataUSAUniversityDataYearModel {
    public int year;
    public DataUSAUniversityGeneralYearModel generalData;

    public List<DataUSAUniversityDegreeModel> degreeData = new ArrayList<>();
    public int totalDegreeCompletions;

    public Map<DataUSAUniversityEnrollmentModel.Race, Integer> enrollmentByRace = new HashMap<>();
    public int totalEnrollment;
    public String universityName;
}
