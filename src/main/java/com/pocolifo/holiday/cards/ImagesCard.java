package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;

import java.io.IOException;

public class ImagesCard implements Card {
    @Override
    public String getName() {
        return "Images";
    }

    @Override
    public String getCardHtml(String phrase) throws IOException {
        StringBuilder b = new StringBuilder();

        Fetchers.imseaImage.fetchData(phrase).forEach(s -> b.append("<img src=\"").append(s).append("\" />"));

        return b.toString();
    }
}
