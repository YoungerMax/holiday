package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.models.SocialMediaProfileModel;

import static com.pocolifo.holiday.Utility.shortenNumber;
import static com.pocolifo.holiday.Utility.shortenString;

public class SocialMediaProfilesCard implements Card {
    @Override
    public String getName() {
        return "Profiles";
    }

    @Override
    public String getCardHtml(String phrase) throws Exception {
        StringBuilder b = new StringBuilder();

        b.append("<div class=\"horiz-scroller\">");

        for (SocialMediaProfileModel profile : Fetchers.socialMediaProfiles.fetchData(phrase)) {
            b.append("<div class=\"sm-card\">");

            b.append("<a href=\"").append(String.format(profile.platform.formatterUrl, profile.identifier)).append("\">");

            b.append("<div style=\"display: flex; align-items: center; gap: 5px;\"><img src=\"")
                    .append(profile.platform.logoUrl).append("\" width=\"48\" height=\"48\" /><h2>")
                    .append(shortenString(profile.name, profile.verified ? 10 : 12)).append("</h2>");

            if (profile.verified) b.append("<img width=\"24\" height=\"24\" title=\"Verified\" src=\"https://img.icons8.com/fluency/48/null/verified-badge.png\"/>");

            b.append("</div></a>");

            b.append("<p>").append(profile.platform.name);

            if (profile.followers > -1) {
                b.append(" \u2022 ").append(shortenNumber(profile.followers)).append("+");
            }

            b.append("</p>");

            b.append("</div>");
        }

        b.append("</div>");
        b.append("<p>(i) <i>Follower count, profile names, and account name data may be out of date.</i></p>");

        return b.toString();
    }
}
