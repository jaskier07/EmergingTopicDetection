package pl.kania.etd.content;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.NoSuchElementException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AugmentedNormalizedTermFrequencyCounter {

    public static double count(String word, Map<String, Integer> wordFrequencies) {
        int wordFrequency = wordFrequencies.get(word);
        int highestFrequency = getHighestFrequency(wordFrequencies);
        return 0.5 + 0.5 * (1. * wordFrequency / highestFrequency);
    }

    private static int getHighestFrequency(Map<String, Integer> wordFrequencies) {
        return wordFrequencies.values()
                .stream()
                .max(Integer::compareTo)
                .orElseThrow(() -> new NoSuchElementException("Error finding highest frequency"));
    }
}
