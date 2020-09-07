package pl.kania.etd.energy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import pl.kania.etd.debug.Counter;
import pl.kania.etd.graph.drop.DropTestFactory;
import pl.kania.etd.graph.drop.TestSet;
import pl.kania.etd.periods.WordStatistics;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EmergingWordSetterTest {

    @Test
    void givenWordStatisticsFindEmergingWordsBasedOnCriticalDrop() {
        List<WordStatistics> wordStatistics = getWordStatistics();

        EmergingWordSetter.setBasedOnCriticalDrop(wordStatistics, 0);
        List<String> emergingWords = wordStatistics.stream()
                .filter(WordStatistics::isEmerging)
                .map(WordStatistics::getWord)
                .collect(Collectors.toList());

        List<String> expectedEmergingWords = Arrays.asList("0", "6", "7");
        Assertions.assertEquals(expectedEmergingWords, emergingWords);
    }

    private List<WordStatistics> getWordStatistics() {
        Counter index = new Counter();
        return new DropTestFactory(TestSet.DEFAULT).getValues().stream()
                .map(v -> {
                    WordStatistics word = new WordStatistics(Integer.toString(index.getValue()));
                    word.setEnergy(v);
                    index.increment();
                    return word;
                }).collect(Collectors.toList());
    }

}