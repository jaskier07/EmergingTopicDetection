package pl.kania.etd.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kania.etd.content.WordFrequenciesInTextCounter;

import java.util.HashMap;
import java.util.Map;

class WordFrequenciesInTextCounterTest {

    @Test
    void givenSentenceSplitItIntoWords() {
        String sentence = "You you yoU are GOING going be'be tested.";

        Map<String, Integer> frequencies = new HashMap<>();
        frequencies.put("you", 3);
        frequencies.put("are", 1);
        frequencies.put("going", 2);
        frequencies.put("be", 2);
        frequencies.put("tested", 1);

        Assertions.assertEquals(WordFrequenciesInTextCounter.count(sentence), frequencies);
    }

}