package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.models.RelatedNewsResultModel;

import java.io.IOException;

public class RelatedNewsCard implements Card {
    @Override
    public String getName() {
        return "Related News";
    }

    @Override
    public String getCardHtml(String phrase) throws IOException {
        StringBuilder b = new StringBuilder();

        b.append("<div style=\"display:flex;gap:6px;width:100%;overflow:auto;\">");

        for (RelatedNewsResultModel result : Fetchers.relatedNewsCard.fetchData(phrase)) {
            b.append("<div class=\"news-card\"><a href=\"").append(result.article.url).append("\">");

            if (result.article.thumbnailUrl != null)
                b.append("<img src=\"").append(result.article.thumbnailUrl).append("\" width=\"200\" />");

            b.append("<div><h3>").append(result.article.title).append("</h3></a>")
                    .append("<p>").append(result.article.description).append("</p>")
                    .append("<p><a href=\"").append(result.article.source.url).append("\">").append(result.article.source.name).append("</a> \u2022 ").append(result.article.date)
                    .append("</p></div></div>");
        }

        b.append("</div>");

        return b.toString();
    }
}
