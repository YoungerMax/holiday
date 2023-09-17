package com.pocolifo.holiday;

import com.google.gson.Gson;
import com.pocolifo.holiday.cards.*;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.fetchers.types.*;
import okhttp3.OkHttpClient;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Holiday {
    public final OkHttpClient httpClient;
    public final OkHttpClient httpClientNoRedir;
    public final Gson gson;

    private final List<Card> cards;

    public Holiday() {
        httpClient = new OkHttpClient().newBuilder()
                .followRedirects(true)
                .build();

        httpClientNoRedir = new OkHttpClient().newBuilder()
                .followRedirects(false)
                .build();

        cards = new ArrayList<>();
        gson = new Gson();

        registerFetchers();
        registerCards();
    }

    private void registerCards() {
        cards.add(new DescriptionCard());
        cards.add(new SummaryCard());
        cards.add(new QuickFactsCard());
        cards.add(new SourceWatchCard());
//        cards.add(new ImagesCard());
        cards.add(new UniversityCard());
        cards.add(new WordCard());
        cards.add(new PlaceCard());
//        cards.add(new RelatedNewsCard());
        cards.add(new WeatherCard());
        cards.add(new MapCard());
        cards.add(new StockMarketCard());
        cards.add(new TOSDRCard());
        cards.add(new SocialMediaProfilesCard());
    }

    private void registerFetchers() {
        Fetchers.wikipediaSearch = new WikipediaSearchFetcher(this);
        Fetchers.wikipediaSummary = new WikipediaSummaryFetcher(this);
        Fetchers.wikipediaInfobox = new WikipediaInfoboxFetcher();
        Fetchers.sourceWatchSearch = new SourceWatchSearchFetcher(this);
        Fetchers.sourceWatchSummary = new SourceWatchSummaryFetcher();
        Fetchers.dataUSASearch = new DataUSASearchFetcher(this);
        Fetchers.dataUSAUniversity = new DataUSAUniversityFetcher(this);
        Fetchers.dataUSAPlace = new DataUSAPlaceFetcher(this);
        Fetchers.dictionary = new DictionaryFetcher(this);
        Fetchers.imseaImage = new ImseaImageFetcher(this);
        Fetchers.relatedNewsCard = new RelatedNewsFetcher(this);
        Fetchers.wikibaseId = new WikibaseIdFetcher();
        Fetchers.wikibaseData = WikibaseDataFetcher.getWikidataDataFetcher();
        Fetchers.placeCoordinates = new PlaceCoordinatesFetcher();
        Fetchers.weather = new WeatherFetcher(this);
        Fetchers.stockTickerSymbol = new StockTickerSymbolFetcher();
        Fetchers.stockTickerQuote = new StockTickerQuoteFetcher(this);
        Fetchers.tosdrSearch = new TOSDRSearchFetcher(this);
        Fetchers.tosdrService = new TOSDRServiceFetcher(this);
        Fetchers.socialMediaProfiles = new SocialMediaProfilesFetcher();
    }

    public void lookup(String phrase, BiConsumer<Card, Object> cardLoadedCallback) {
        cards.parallelStream().forEach(card -> {
            try {
                String cardHtml = card.getCardHtml(phrase);

                cardLoadedCallback.accept(card, cardHtml);
            } catch (NotApplicableException e) {
                // Not applicable
                // Do nothing
            } catch (Exception e) {
                cardLoadedCallback.accept(card, e);
            }
        });
    }
}
