package com.pocolifo.holiday.models;

import com.google.gson.annotations.SerializedName;

// TODO: add more https://query1.finance.yahoo.com/v7/finance/quote?symbols=aapl
public class StockMarketQuoteModel {
    public enum MarketState {
        @SerializedName("CLOSED")
        CLOSED,

        @SerializedName("OPEN")
        OPEN;
    }

    @SerializedName("quoteSourceName")
    public String sourceName;

    @SerializedName("shortName")
    public String shortName;

    @SerializedName("displayName")
    public String displayName;

    @SerializedName("regularMarketPrice")
    public float price;

    @SerializedName("regularMarketChangePercent")
    public float percentChange;

    @SerializedName("regularMarketChange")
    public float moneyChange;

    @SerializedName("marketState")
    public MarketState marketState;

    @SerializedName("sourceInterval")
    public int sourceInterval;

    @SerializedName("regularMarketDayHigh")
    public float dayHigh;

    @SerializedName("regularMarketDayLow")
    public float dayLow;

    @SerializedName("regularMarketVolume")
    public long volume;

    @SerializedName("regularMarketPreviousClose")
    public float previousClosePrice;

    @SerializedName("regularMarketOpen")
    public float openPrice;

    @SerializedName("marketCap")
    public long marketCap;

    @SerializedName("currency")
    public String currency;
}
