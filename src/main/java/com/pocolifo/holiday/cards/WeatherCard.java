package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.types.WeatherFetcher;
import com.pocolifo.holiday.models.WeatherForecastModel;
import com.pocolifo.holiday.models.WeatherModel;

import java.util.List;

public class WeatherCard implements Card {
    @Override
    public String getName() {
        return "Weather";
    }

    @Override
    public String getCardHtml(String phrase) throws Exception {
        Fetchers.weather.unit = WeatherFetcher.TemperatureUnit.FAHRENHEIT;
        WeatherModel weather = Fetchers.weather.fetchData(phrase);

        StringBuilder b = new StringBuilder();

        b.append("<h2>").append(weather.location.formattedName).append("</h2>")
                .append("<h1>").append(weather.currentConditions.temperature).append("&#176;").append(Fetchers.weather.unit.abbreviated).append(" \u2022 ").append(weather.currentConditions.description).append("</h1>");

        // forecast table
        b.append("<div class=\"horiz-scroller\">");

        List<WeatherForecastModel> days = weather.forecast.days;
        for (int i = 1; i < days.size(); i++) {
            WeatherForecastModel m = days.get(i);
            b.append("<div class=\"sm-card\"><h3>").append(m.dayOfTheWeek).append("</h3>")
                    .append("<p><strong>")
                    .append(m.high).append("&#176;").append(Fetchers.weather.unit.abbreviated)
                    .append("</strong> \u2022 ")
                    .append(m.low).append("&#176;").append(Fetchers.weather.unit.abbreviated)
                    .append("</p>")

                    .append("<p>")
                    .append(m.description)
                    .append(" \u2022 ")
                    .append(m.descriptionNight)
                    .append("</p></div>");
        }

        b.append("</div><p>As of ").append(weather.currentConditions.lastUpdatedTime.timeString).append("</p>");

        return b.toString();
    }
}
