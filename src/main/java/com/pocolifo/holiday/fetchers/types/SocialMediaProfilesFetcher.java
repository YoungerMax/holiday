package com.pocolifo.holiday.fetchers.types;

import com.pocolifo.holiday.Utility;
import com.pocolifo.holiday.fetchers.core.Fetcher;
import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.fetchers.core.NotApplicableException;
import com.pocolifo.holiday.models.SocialMediaPlatformModel;
import com.pocolifo.holiday.models.SocialMediaProfileModel;
import org.wikidata.wdtk.datamodel.interfaces.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SocialMediaProfilesFetcher implements Fetcher<List<SocialMediaProfileModel>> {
    public static final SocialMediaPlatformModel[] PLATFORMS = {
            new SocialMediaPlatformModel("Telegram", "/telegram.svg", "P3789", " https://t.me/$%s"),
            new SocialMediaPlatformModel("Twitter", "/twitter.svg", "P2002", "https://twitter.com/%s"),
            new SocialMediaPlatformModel("Facebook", "/facebook.svg", "P2013", "https://www.facebook.com/%s"),
            new SocialMediaPlatformModel("Instagram", "/instagram.svg", "P2003", "https://www.instagram.com/%s"),
            new SocialMediaPlatformModel("YouTube", "/youtube.svg", "P2397", "https://www.youtube.com/channel/%s"),
            new SocialMediaPlatformModel("TikTok", "/tiktok.svg", "P7085", "https://www.tiktok.com/@%s"),
            new SocialMediaPlatformModel("Flickr", "/flickr.svg", "P3267", "https://www.flickr.com/people/%s"),
            new SocialMediaPlatformModel("Spotify", "/spotify.svg", "P1902", "https://open.spotify.com/artist/%s"),
            new SocialMediaPlatformModel("SoundCloud", "/soundcloud.svg", "P3040", "https://soundcloud.com/%s")
    };

    public static final String HAS_QUALITY = "P1552";
    public static final String SUBSCRIBER_FOLLOWER_COUNT = "P3744";
    public static final String SUBJECT_NAMED_AS = "P1810";
    public static final String VERIFIED_ACCOUNT = "Q28378282";
    public static final String VERIFIED_SPOTIFY_ACCOUNT = "Q98962768";

    @Override
    public synchronized List<SocialMediaProfileModel> fetchData(String phrase) throws Exception {
        EntityDocument doc = Fetchers.wikibaseData.getEntityDocument(Fetchers.wikibaseId.fetchData(phrase));

        if (doc instanceof ItemDocument) {
            List<SocialMediaProfileModel> profiles = new ArrayList<>();
            ItemDocument item = (ItemDocument) doc;

            for (SocialMediaPlatformModel platform : PLATFORMS) {
                StatementGroup group = item.findStatementGroup(platform.wikidataProp);

                if (group == null) continue;

                for (Statement statement : group.getStatements()) {
                    SocialMediaProfileModel model = new SocialMediaProfileModel();

                    model.platform = platform;

                    // GET ID
                    model.identifier = statement.getMainSnak().accept(new Utility.DefaultSnakVisitor<>() {
                        @Override
                        public String visit(ValueSnak snak) {
                            return snak.getValue().accept(new Utility.DefaultValueVisitor<>() {
                                @Override
                                public String visit(StringValue value) {
                                    return value.getString();
                                }
                            });
                        }
                    });

                    Iterator<Snak> qualifiers = statement.getAllQualifiers();

                    while (qualifiers.hasNext()) {
                        Snak next = qualifiers.next();

                        switch (next.getPropertyId().getId()) {
                            // GET VERIFICATION STATUS
                            case HAS_QUALITY:
                                model.verified = next.accept(new Utility.DefaultSnakVisitor<>() {
                                    @Override
                                    public Boolean visit(ValueSnak snak) {
                                        return snak.getValue().accept(new Utility.DefaultValueVisitor<>() {
                                            @Override
                                            public Boolean visit(EntityIdValue value) {
                                                return value.getId().equals(VERIFIED_ACCOUNT) || value.getId().equals(VERIFIED_SPOTIFY_ACCOUNT);
                                            }
                                        });
                                    }
                                });

                                break;

                            case SUBSCRIBER_FOLLOWER_COUNT:
                                model.followers = next.accept(new Utility.DefaultSnakVisitor<>() {
                                    @Override
                                    public Integer visit(ValueSnak snak) {
                                        return snak.getValue().accept(new Utility.DefaultValueVisitor<>() {
                                            @Override
                                            public Integer visit(QuantityValue value) {
                                                return value.getNumericValue().intValue();
                                            }
                                        });
                                    }
                                });

                                break;

                            case SUBJECT_NAMED_AS:
                                model.name = next.accept(new Utility.DefaultSnakVisitor<>() {
                                    @Override
                                    public String visit(ValueSnak snak) {
                                        return snak.getValue().accept(new Utility.DefaultValueVisitor<>() {
                                            @Override
                                            public String visit(StringValue value) {
                                                return value.getString();
                                            }
                                        });
                                    }
                                });

                                break;
                        }
                    }

                    if (model.name == null) model.name = model.identifier;

                    // ADD
                    profiles.add(model);
                }
            }

            if (profiles.isEmpty()) throw new NotApplicableException();

            return profiles;
        }

        throw new NotApplicableException();
    }
}
