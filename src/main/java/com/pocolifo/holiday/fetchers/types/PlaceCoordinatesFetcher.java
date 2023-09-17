package com.pocolifo.holiday.fetchers.types;

import com.pocolifo.holiday.Utility;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.GlobeCoordinatesModel;
import org.apache.commons.compress.utils.Iterators;
import org.wikidata.wdtk.datamodel.interfaces.*;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public class PlaceCoordinatesFetcher implements Fetcher<GlobeCoordinatesModel> {
    public static final String COORDINATES_PROPERTY = "P625";

    @Override
    public synchronized GlobeCoordinatesModel fetchData(String phrase) throws Exception {
        String wikibaseId = Fetchers.wikibaseId.fetchData(phrase);

        // get wikibase data
        EntityDocument doc = Fetchers.wikibaseData.getEntityDocument(wikibaseId);

        if (doc instanceof ItemDocument) {
            ItemDocument item = (ItemDocument) doc;
            GlobeCoordinatesValue coordinatesValue = item.findStatementGlobeCoordinatesValue(COORDINATES_PROPERTY);

            if (coordinatesValue == null) throw new NotApplicableException();

            // Populate and return the model!
            GlobeCoordinatesModel m = new GlobeCoordinatesModel();

            m.latitude = coordinatesValue.getLatitude();
            m.longitude = coordinatesValue.getLongitude();

            return m;
        }

        throw new NotApplicableException();
    }
}
