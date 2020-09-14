package pl.kania.etd.energy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.kania.etd.debug.Counter;
import pl.kania.etd.debug.NumberFormatter;
import pl.kania.etd.graph.drop.AdaptiveCutOff;
import pl.kania.etd.periods.TimePeriod;
import pl.kania.etd.periods.TimePeriods;
import pl.kania.etd.periods.WordStatistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmergingWordSetter {

    public static void setBasedOnThreshold(TimePeriod period, double thresholdEnergy) {
        double dropInPeriod = thresholdEnergy * sumEnergy(period) / allWordsInPeriod(period);
        period.setDropInPeriod(dropInPeriod);

        Counter ctr = new Counter();
        period.getWordStatistics().values().forEach(word -> {
            if (word.getEnergy() > dropInPeriod) {
                word.setEmerging(true);
                ctr.increment();
            }
        });

        log.info("Period #" + period.getIndex() + ": drop(" + NumberFormatter.format(dropInPeriod, 7) + "), emergent %( " +
                NumberFormatter.formatPercentage(ctr.getValue(), period.getWordStatistics().size()) + "), emergent(" + ctr.getValue() + ") " + period.getTimeRange());
    }

    public static void setBasedOnCriticalDrop(Collection<WordStatistics> statistics, int periodIndex) {
        AdaptiveCutOff.performActionForElementsBeforeCriticalDrop(statistics, list -> list.forEach(w -> w.setEmerging(true)));
        long emergingCount = statistics.stream()
                .filter(WordStatistics::isEmerging)
                .count();
        log.info("Emerging words in period #" + periodIndex + ": " + NumberFormatter.formatPercentage(emergingCount, statistics.size())
                + "(" + emergingCount + "/" + statistics.size() + ")");
    }

    private static double sumEnergy(TimePeriod period) {
        if (period.getWordStatistics().isEmpty()) {
            return 0;
        }
        return period.getWordStatistics().values().stream()
                .map(WordStatistics::getEnergy)
                .reduce(Double::sum)
                .orElseThrow(() -> new IllegalStateException("Error summing energy in period"));
    }

    private static int allWordsInPeriod(TimePeriod period) {
        return period.getWords().size();
    }
}
