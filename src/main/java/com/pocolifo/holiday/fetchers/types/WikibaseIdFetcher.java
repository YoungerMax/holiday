package com.pocolifo.holiday.fetchers.types;

import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.WikipediaSummaryModel;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;

import java.util.List;

public class WikibaseIdFetcher implements Fetcher<String> {
    private String cache;

    @Override
    public synchronized String fetchData(String phrase) throws Exception {
        if (cache != null) return cache;

        WikipediaSummaryModel summaryModel = Fetchers.wikipediaSummary.fetchData(phrase);

        if (summaryModel == null) {
            List<WbSearchEntitiesResult> results = Fetchers.wikibaseData.searchEntities(phrase);

            if (results.isEmpty()) throw new NotApplicableException();

            cache = results.get(0).getEntityId();
        } else {
            cache = summaryModel.wikibaseItem;
        }

        if (cache == null) throw new NotApplicableException();

        return cache;
    }
}
