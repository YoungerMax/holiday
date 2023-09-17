package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;

import java.io.IOException;

public class SourceWatchCard implements Card {
    @Override
    public String getName() {
        return "SourceWatch";
    }

    @Override
    public String getCardHtml(String phrase) throws IOException {
        return Fetchers.sourceWatchSummary.fetchData(phrase);
    }
}
