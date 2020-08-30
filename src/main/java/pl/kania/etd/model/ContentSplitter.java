package pl.kania.etd.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.jbosslog.JBossLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.util.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentSplitter {

    public static Set<Word> splitIntoWords(String text) {
        Map<String, Integer> wordFrequencies = WordFrequenciesInTextCounter.count(text);
        Set<Word> words = new HashSet<>();

        wordFrequencies.forEach((key, value) -> {
            double weight = AugmentedNormalizedTermFrequencyCounter.count(key, wordFrequencies);
            words.add(new Word(key, weight));
        });

        return words;
    }

}
