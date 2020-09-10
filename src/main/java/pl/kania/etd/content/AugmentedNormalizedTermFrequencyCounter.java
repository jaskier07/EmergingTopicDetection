package pl.kania.etd.content;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.NoSuchElementException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AugmentedNormalizedTermFrequencyCounter {

    public static<T> double count(T word, Map<T, Integer> wordFrequencies) {
        int wordFrequency = wordFrequencies.get(word);
        int highestFrequency = getHighestFrequency(wordFrequencies);
        if (highestFrequency == 0) {
            return 0;
        }
        return 0.5 + 0.5 * (1. * wordFrequency / highestFrequency);
    }

    public static<T> double count(int wordFrequency, int highestFrequency) {
        if (highestFrequency == 0) {
            return 0;
        }
        return 0.5 + 0.5 * (1. * wordFrequency / highestFrequency);
    }

    public static<T> int getHighestFrequency(Map<T, Integer> wordFrequencies) {
        return wordFrequencies.values()
                .stream()
                .max(Integer::compareTo)
                .orElse(0);
    }
}
