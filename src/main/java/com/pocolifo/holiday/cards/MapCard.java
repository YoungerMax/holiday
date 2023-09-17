package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.models.GlobeCoordinatesModel;
import com.pocolifo.holiday.models.WikipediaSummaryModel;

public class MapCard implements Card {
    @Override
    public String getName() {
        return "Map";
    }

    @Override
    public String getCardHtml(String phrase) throws Exception {
        GlobeCoordinatesModel coords = Fetchers.placeCoordinates.fetchData(phrase);

        return "<iframe width=\"600\" height=\"500\" src=\"https://maps.google.com/maps?q=" + coords.latitude + "%20" + coords.longitude + "&t=&z=12&ie=UTF8&iwloc=&output=embed\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"></iframe>";
    }
}
