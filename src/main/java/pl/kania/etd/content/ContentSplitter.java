package pl.kania.etd.content;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.NumberUtils;

import java.util.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentSplitter {

    public static Set<Word> splitIntoWords(String text) {
        Map<String, Integer> wordFrequencies = WordFrequenciesInTextCounter.count(text);
        Set<Word> words = new HashSet<>();

        wordFrequencies.forEach((key, value) -> {
            if (meetsMinLengthCondition(key)) {
                double weight = AugmentedNormalizedTermFrequencyCounter.count(key, wordFrequencies);
                words.add(new Word(key, weight));
            }
        });

        return words;
    }

    private static boolean meetsMinLengthCondition(String key) {
        return key.length() >= 3;
    }

}
