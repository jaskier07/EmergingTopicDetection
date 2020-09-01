package pl.kania.etd.energy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.WordStatistics;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

class EnergyCounterTest {

    public static final double DELTA = 0.01;

    @Test
    void givenTweetsInPeriodsCountItsWordEnergy() {
        TimePeriod timePeriod1 = new TimePeriod(0, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        TimePeriod timePeriod2 = new TimePeriod(1, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        TimePeriod timePeriod3 = new TimePeriod(2, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3));

        addWordStatisticsToTimePeriod("dog", timePeriod1, 0.3);
        addWordStatisticsToTimePeriod("dog", timePeriod2, 0.4);
        addWordStatisticsToTimePeriod("dog", timePeriod3, 0.5);

        addWordStatisticsToTimePeriod("cat", timePeriod1, 0.2);
        addWordStatisticsToTimePeriod("cat", timePeriod3, 0.2);

        List<TimePeriod> periods = Arrays.asList(timePeriod1, timePeriod2, timePeriod3);
        for (int periodIndex = periods.size() - 1; periodIndex >= 0; periodIndex--) {
            EnergyCounter.count(periods, periodIndex, 2);
        }

        Assertions.assertEquals(0., getWordEnergyInPeriod("dog", timePeriod1), DELTA);
        Assertions.assertEquals(0.07, getWordEnergyInPeriod("dog", timePeriod2), DELTA);
        Assertions.assertEquals(0.17, getWordEnergyInPeriod("dog", timePeriod3), DELTA);
        Assertions.assertEquals(0., getWordEnergyInPeriod("cat", timePeriod1), DELTA);
        Assertions.assertEquals(0, getWordEnergyInPeriod("cat", timePeriod2), DELTA);
        Assertions.assertEquals(0.04, getWordEnergyInPeriod("cat", timePeriod3), DELTA);
    }

    private double getWordEnergyInPeriod(String word, TimePeriod timePeriod1) {
        WordStatistics wordStatistics = timePeriod1.getWordStatistics().get(word);
        if (wordStatistics == null) {
            return 0;
        }
        return wordStatistics.getEnergy();
    }

    private WordStatistics addWordStatisticsToTimePeriod(String word, TimePeriod timePeriod1, double nutrition) {
        WordStatistics statistics = new WordStatistics(word);
        statistics.setNutrition(nutrition);
        return timePeriod1.getWordStatistics().put(word, statistics);
    }

}