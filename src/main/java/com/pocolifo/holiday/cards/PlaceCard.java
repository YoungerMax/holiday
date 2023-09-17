package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.models.DataUSAPlaceDataYearModel;

import java.io.IOException;

import static com.pocolifo.holiday.Utility.formatNumber;
import static com.pocolifo.holiday.Utility.roundToNearestHundredthPercentage;

public class PlaceCard implements Card {
    @Override
    public String getName() {
        return "Place";
    }

    @Override
    public String getCardHtml(String phrase) throws IOException {
        DataUSAPlaceDataYearModel year = Fetchers.dataUSAPlace.fetchData(phrase).get(0);

        StringBuilder b = new StringBuilder(
                "<h1>" + year.placeName + "</h1>" +
                "<h2>" + year.year + " data</h2>" +
                "<p><strong>Average household income</strong>: $" + formatNumber(year.averageHouseholdIncome) + "</p>" +
                "<p><strong>Median property value</strong>: $" + formatNumber(year.medianPropertyValue) + "</p>" +
                "<h2>Population</h2>" +
                "<p><strong>Total population</strong>: " + formatNumber(year.totalPopulation) + "</p>");

        year.population.forEach((raceEthnicityPair, population) -> {
            float percentage = roundToNearestHundredthPercentage((float) population / (float) year.totalPopulation);

            b.append("<p><strong>").append(raceEthnicityPair.race.name).append(" & ").append(raceEthnicityPair.ethnicity.name)
                    .append("</strong>: ").append(percentage).append("% (").append(population).append("/")
                    .append(formatNumber(year.totalPopulation)).append(")</p>");
        });

        return b.toString();
    }
}
