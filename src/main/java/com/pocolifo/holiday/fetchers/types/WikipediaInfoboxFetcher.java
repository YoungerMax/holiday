package com.pocolifo.holiday.fetchers.types;

import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.WikipediaInfoboxModel;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class WikipediaInfoboxFetcher implements Fetcher<WikipediaInfoboxModel> {
    private WikipediaInfoboxModel model = null;

    @Override
    public synchronized WikipediaInfoboxModel fetchData(String phrase) throws IOException {
        // Check cache
        if (model != null) return model;

        // Get data
        WikipediaInfoboxModel infobox = new WikipediaInfoboxModel();

        Document document = Jsoup.connect(Fetchers.wikipediaSearch.fetchData(phrase).get(0).url).get();
        Elements element = document.select("table.infobox");

        infobox.title = element.select(".infobox-title").text();
        infobox.subheader = element.select(".infobox-subheader").text();

        element.select("img").stream().filter(node -> !node.hasAttr("class")).forEach(imgElement -> infobox.imageUrls.add(imgElement.attr("src")));

        element.select("tr").stream().filter(node -> {
            boolean containsLabel = node.childNodes().stream().anyMatch(n -> n.attr("class").contains("infobox-label"));
            boolean containsData = node.childNodes().stream().anyMatch(n -> n.attr("class").contains("infobox-data"));

            return containsLabel && containsData;
        }).forEach(row -> {
            Elements label = row.select(".infobox-label");
            Elements data = row.select(".infobox-data");
            List<String> value;

            if (data.select("li").size() > 1) {
                value = data.select("li").stream().map(Element::text).collect(Collectors.toList());
            } else {
                value = Collections.singletonList(data.text());
            }

            infobox.data.put(label.text(), value);
        });

        // map card already has this
        infobox.data.remove("Location");
        infobox.data.remove("Coordinates");
        infobox.data.remove("Address");

        if (infobox.data.isEmpty()) throw new NotApplicableException();

        // Success! return
        this.model = infobox;

        return model;
    }
}
