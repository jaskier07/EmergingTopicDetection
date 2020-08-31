package pl.kania.etd.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kania.etd.content.AugmentedNormalizedTermFrequencyCounter;

import java.util.HashMap;
import java.util.Map;

class AugmentedNormalizedTermFrequencyCounterTest {

    private static final double DELTA = 0.01;

    @Test
    public void givenSentenceGetNormalizedTermFrequency() {
        Map<String, Integer> frequencies = new HashMap<>();
        frequencies.put("you", 3);
        frequencies.put("going", 2);
        frequencies.put("are", 1);

        Assertions.assertEquals(getFrequency("you", frequencies), 1., DELTA);
        Assertions.assertEquals(getFrequency("going", frequencies), 0.83, DELTA);
        Assertions.assertEquals(getFrequency("are", frequencies), 0.66, DELTA);

    }

    private double getFrequency(String word, Map<String, Integer> frequencies) {
        return AugmentedNormalizedTermFrequencyCounter.count(word, frequencies);
    }

}