package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.models.DataUSAUniversityDataYearModel;
import com.pocolifo.holiday.models.DataUSAUniversityDegreeModel;

import java.io.IOException;

import static com.pocolifo.holiday.Utility.formatNumber;
import static com.pocolifo.holiday.Utility.roundToNearestHundredthPercentage;

public class UniversityCard implements Card {
    @Override
    public String getName() {
        return "University";
    }

    @Override
    public String getCardHtml(String phrase) throws IOException {
        DataUSAUniversityDataYearModel year = Fetchers.dataUSAUniversity.fetchData(phrase).get(0);

        float acceptanceRate = roundToNearestHundredthPercentage((float) year.generalData.totalAdmissions / (float) year.generalData.totalApplicants);

        StringBuilder s = new StringBuilder(
                "<h1>" + year.universityName + "</h1>" +
                "<h2>" + year.year + " data</h2>" +
                "<p><strong>Acceptance rate</strong>: " + acceptanceRate + "% (" + formatNumber(year.generalData.totalAdmissions) + "/" + formatNumber(year.generalData.totalApplicants) + ")</p>" +
                "<p><strong>Average tuition</strong>: $" + formatNumber(year.generalData.tuitionCost) + "</p>" +
                "<p><strong>Accepted SAT Math scores</strong>: " + year.generalData.satMath25thPercentile + "-" + year.generalData.satMath75thPercentile + "</p>" +
                "<p><strong>Accepted SAT Reading scores</strong>: " + year.generalData.satReading25thPercentile + "-" + year.generalData.satReading75thPercentile + "</p>" +

                "<h2>Enrollment by race</h2>");

        year.enrollmentByRace.forEach((race, enrollment) -> {
            float percentEnrollment = roundToNearestHundredthPercentage((float) enrollment / (float) year.totalEnrollment);

            s.append("<p><strong>").append(race.name).append("</strong>: ").append(percentEnrollment).append("% (").append(formatNumber(enrollment)).append("/").append(formatNumber(year.totalEnrollment)).append(")</p>");
        });

        // degrees - show top 5
        s.append("<h2>Degrees</h2>");
        for (int i = 0; Math.min(year.degreeData.size(), 5) > i; i++) {
            DataUSAUniversityDegreeModel degree = year.degreeData.get(i);
            float percentCompletions = roundToNearestHundredthPercentage((float) degree.completions / (float) year.totalDegreeCompletions);

            s.append("<p><strong>").append(degree.degreeName).append("</strong>: ").append(percentCompletions).append("% (").append(formatNumber(degree.completions)).append("/").append(formatNumber(year.totalDegreeCompletions)).append(")</p>");
        }

        s.append("<details><summary>Expand the rest</summary>");

        for (int i = 5; year.degreeData.size() > i; i++) {
            DataUSAUniversityDegreeModel degree = year.degreeData.get(i);
            float percentCompletions = roundToNearestHundredthPercentage((float) degree.completions / (float) year.totalDegreeCompletions);

            s.append("<p><strong>").append(degree.degreeName).append("</strong>: ").append(percentCompletions).append("% (").append(formatNumber(degree.completions)).append("/").append(formatNumber(year.totalDegreeCompletions)).append(")</p>");
        }

        s.append("</details><p>(i) <i>Colleges may misreport data in order to attract a specific demographic of students.</i></p>");

        return s.toString();
    }
}
