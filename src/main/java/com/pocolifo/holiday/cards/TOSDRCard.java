package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.models.TOSDRPointModel;
import com.pocolifo.holiday.models.TOSDRServiceModel;

public class TOSDRCard implements Card {
    @Override
    public String getName() {
        return "Terms of Service";
    }

    @Override
    public String getCardHtml(String phrase) throws Exception {
        TOSDRServiceModel service = Fetchers.tosdrService.fetchData(phrase);
        StringBuilder b = new StringBuilder();

        b.append("<h1 style=\"display:flex;align-items:center;\">").append(service.name);

        if (service.isComprehensivelyReviewed) b.append("<img width=\"24\" height=\"24\" title=\"Comprehensively reviewed by TOSDR team\" src=\"https://img.icons8.com/fluency/48/null/verified-badge.png\"/>");

        b.append("</h1>");

        String grade;

        if (2 > service.rating) {
            grade = "<span style=\"color: #22c55e;\">Excellent</span>";
        } else if (3 > service.rating) {
            grade = "<span style=\"color: #84cc16;\">Good</span>";
        } else if (6 > service.rating) {
            grade = "<span style=\"color: #f59e0b;\">Fair</span>";
        } else if (10 > service.rating) {
            grade = "<span style=\"color: #f97316;\">Bad</span>";
        } else {
            grade = "<span style=\"color: #ef4444;\">Poor</span>";
        }

        b.append("<h3>Privacy Rating: ").append(grade).append("</h3>");
        b.append("<h3>Main points</h3><ul>");

        service.points.stream().filter(model -> model.status == TOSDRPointModel.TOSDRPointStatus.APPROVED).forEach(model -> {
            b.append("<li>").append(model.title).append("</li>");
        });

        b.append("</ul>");

        return b.toString();
    }
}
