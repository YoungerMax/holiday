package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;

public class DescriptionCard implements Card {
    @Override
    public String getName() {
        return "Description";
    }

    @Override
    public synchronized String getCardHtml(String phrase) throws Exception {
        EntityDocument doc = Fetchers.wikibaseData.getEntityDocument(Fetchers.wikibaseId.fetchData(phrase));

        if (doc instanceof ItemDocument) {
            ItemDocument item = (ItemDocument) doc;

            return "<p>" + item.getDescriptions().get("en").getText() + "</p>";
        }

        throw new NotApplicableException();
    }
}
