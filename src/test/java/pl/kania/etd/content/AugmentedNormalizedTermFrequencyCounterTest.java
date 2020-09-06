package pl.kania.etd.content;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AugmentedNormalizedTermFrequencyCounterTest {

    private static Map<String, Integer> wordFrequencies;

    @BeforeAll
    static void init() {
        wordFrequencies = new HashMap<>();
        wordFrequencies.put("dog", 10);
        wordFrequencies.put("cat", 5);
        wordFrequencies.put("bird", 1);
    }

    @Test
    void givenWordsAndFrequenciesCheckIfDogsANTFValueIs_1() {
        Assertions.assertEquals(1., AugmentedNormalizedTermFrequencyCounter.count("dog", wordFrequencies));
    }

    @Test
    void givenWordsAndFrequenciesCheckIfCatsANTFValueIs_0_75() {
        Assertions.assertEquals(0.75, AugmentedNormalizedTermFrequencyCounter.count("cat", wordFrequencies));
    }

    @Test
    void givenWordsAndFrequenciesCheckIfBirdsANTFValueIs_0_55() {
        Assertions.assertEquals(0.55, AugmentedNormalizedTermFrequencyCounter.count("bird", wordFrequencies));
    }
}