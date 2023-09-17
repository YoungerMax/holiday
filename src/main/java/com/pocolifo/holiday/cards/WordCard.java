package com.pocolifo.holiday.cards;

import com.pocolifo.holiday.fetchers.core.Fetchers;
import com.pocolifo.holiday.models.DictionaryDefinitionModel;
import com.pocolifo.holiday.models.DictionaryMeaningModel;
import com.pocolifo.holiday.models.DictionaryPhoneticModel;
import com.pocolifo.holiday.models.DictionaryWordModel;

import java.io.IOException;

public class WordCard implements Card {
    @Override
    public String getName() {
        return "Word";
    }

    @Override
    public String getCardHtml(String phrase) throws IOException {
        DictionaryWordModel word = Fetchers.dictionary.fetchData(phrase);
        StringBuilder b = new StringBuilder();

        b.append("<h2>Definition of ").append(word.word).append("</h2>");

        if (word.phonetic != null) b.append("<p>").append(word.phonetic).append("</p>");

        for (DictionaryMeaningModel meaning : word.meanings) {
            b.append("<h3>").append(meaning.partOfSpeech).append("</h3><ul>");

            for (DictionaryDefinitionModel definition : meaning.definition) {
                b.append("<li>").append(definition.definition).append("<ul>");

                if (definition.exampleSentence != null) b.append("<li><strong>Example</strong>: ").append(definition.exampleSentence).append("</li>");
                if (definition.synonyms.length > 0) b.append("<li><strong>Synonyms</strong>: ").append(String.join(", ", definition.synonyms)).append("</li>");
                if (definition.antonyms.length > 0) b.append("<li><strong>Antonyms</strong>: ").append(String.join(", ", definition.antonyms)).append("</li>");

                b.append("</ul></li>");
            }

            b.append("</ul>");
        }

        if (!word.phonetics.isEmpty()) b.append("<h2>Pronunciations</h2>");

        for (DictionaryPhoneticModel phonetic : word.phonetics) {
            if (phonetic.phonetic != null) b.append("<p>").append(phonetic.phonetic).append("</p>");
            if (phonetic.audioUrl != null) b.append("<audio controls><source src=\"").append(phonetic.audioUrl).append("\" /></audio>");
        }

        return b.toString();
    }
}
