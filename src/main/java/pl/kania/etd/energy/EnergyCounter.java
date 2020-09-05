package pl.kania.etd.energy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.TimePeriods;
import pl.kania.etd.periods.WordStatistics;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnergyCounter {

    public static void countAndSet(List<TimePeriod> periods, int currentPeriodIndex, int previousSlotsToConsider) {
        TimePeriod currentPeriod = periods.get(currentPeriodIndex);

        Set<String> words = currentPeriod.getWordStatistics().keySet();
        words.forEach(word -> {
            double wordEnergy = 0.;

            if (currentPeriodIndex != 0) {
                double currentNutrition = getNutrition(word, currentPeriod);
                for (int periodIndex = currentPeriodIndex - 1; periodIndex >= currentPeriodIndex - previousSlotsToConsider && periodIndex >= 0; periodIndex--) {
                    TimePeriod period = periods.get(periodIndex);
                    double nutrition = getNutrition(word, period);
                    wordEnergy += (Math.pow(currentNutrition, 2) - Math.pow(nutrition, 2)) / (currentPeriodIndex - periodIndex);
                }
            }

            currentPeriod.getWordStatistics().get(word).setEnergy(wordEnergy);
        });
    }

    private static double getNutrition(String word, TimePeriod period) {
        WordStatistics wordStatistics = period.getWordStatistics().get(word);
        if (wordStatistics == null) {
            return 0;
        }
        return wordStatistics.getNutrition();
    }

}
