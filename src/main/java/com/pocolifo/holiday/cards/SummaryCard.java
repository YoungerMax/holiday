package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.models.WikipediaSummaryModel;

import java.io.IOException;

public class SummaryCard implements Card {
    @Override
    public String getName() {
        return "Summary";
    }

    @Override
    public String getCardHtml(String phrase) throws IOException {
        WikipediaSummaryModel summary = Fetchers.wikipediaSummary.fetchData(phrase);

        return summary.extractHtml;
    }
}
