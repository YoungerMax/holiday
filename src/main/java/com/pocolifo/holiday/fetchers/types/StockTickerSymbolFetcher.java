package com.pocolifo.holiday.fetchers.types;

import com.pocolifo.holiday.Utility;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import org.wikidata.wdtk.datamodel.interfaces.*;

public class StockTickerSymbolFetcher implements Fetcher<String> {
    public static final String STOCK_EXCHANGE_PROP = "P414";
    public static final String TICKER_SYMBOL_PROP = "P249";

    @Override
    public synchronized String fetchData(String phrase) throws Exception {
        String wikibaseId = Fetchers.wikibaseId.fetchData(phrase);
        EntityDocument doc = Fetchers.wikibaseData.getEntityDocument(wikibaseId);

        if (doc instanceof ItemDocument) {
            ItemDocument item = (ItemDocument) doc;
            Statement statement = null;

            try {
                statement = item.findStatementGroup(STOCK_EXCHANGE_PROP)
                        .stream()
                        .findFirst()
                        .get(); // do not replace with stream API, item.findStatementGroup can return null as well
            } catch (NullPointerException e) {
                throw new NotApplicableException();
            }

            Snak tickerSymbolSnak = null;

            // find the ticker symbol prop, set to var above
            while (statement.getAllQualifiers().hasNext()) {
                Snak snak = statement.getAllQualifiers().next();

                if (snak.getPropertyId().getId().equals(TICKER_SYMBOL_PROP)) {
                    tickerSymbolSnak = snak;
                    break;
                }
            }

            if (tickerSymbolSnak == null) throw new NotApplicableException();

            // get the snak string value (warning: ugly af)
            String result = tickerSymbolSnak.accept(new Utility.DefaultSnakVisitor<>() {
                @Override
                public String visit(ValueSnak snak) {
                    return snak.getValue().accept(new Utility.DefaultValueVisitor<>() {
                        @Override
                        public String visit(StringValue value) {
                            return value.getString().replaceAll("\\.", "-");
                        }
                    });
                }
            });

            if (result == null) throw new NotApplicableException();

            return result;
        }

        throw new NotApplicableException();
    }
}

