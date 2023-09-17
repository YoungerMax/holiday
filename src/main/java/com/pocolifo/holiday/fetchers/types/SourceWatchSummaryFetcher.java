package com.pocolifo.holiday.fetchers.types;

import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;

import java.io.IOException;
import java.util.stream.Collectors;

public class SourceWatchSummaryFetcher implements Fetcher<String> {
    @Override
    public synchronized String fetchData(String phrase) throws IOException {
        Document document = Jsoup.connect(Fetchers.sourceWatchSearch.fetchData(phrase).url).get();

        Elements paragraphs = document.selectFirst(".mw-parser-output").select(new Evaluator() {
            @Override
            public boolean matches(@NotNull Element root, @NotNull Element element) {
                return element.tagName().matches("p") && element.parent().equals(root);
            }
        });

        String s = paragraphs.stream().limit(2).map(Element::outerHtml).collect(Collectors.joining());

        if (s.isEmpty()) throw new NotApplicableException();

        return s;
    }
}
