package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.models.WikipediaInfoboxModel;

import java.io.IOException;
import java.util.stream.Collectors;

public class QuickFactsCard implements Card {
    @Override
    public String getName() {
        return "Quick Facts";
    }

    @Override
    public String getCardHtml(String phrase) throws IOException {
        WikipediaInfoboxModel infobox = Fetchers.wikipediaInfobox.fetchData(phrase);

        StringBuilder builder = new StringBuilder();

        infobox.data.forEach((key, val) -> {
            builder.append("<p><strong>").append(key).append("</strong><br />");
            builder.append(val.stream().collect(Collectors.joining("<br />")));
        });

        return builder.toString();
    }
}
