package com.pocolifo.holiday.cards;

public interface Card {
    String getName();

    String getCardHtml(String phrase) throws Exception;
}
